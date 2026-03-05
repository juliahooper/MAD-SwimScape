package com.swimscape.repository

import com.swimscape.data.firestore.FirestoreDataSource
import com.swimscape.data.room.AlertDao
import com.swimscape.data.room.AlertEntity
import com.swimscape.data.room.FavouriteDao
import com.swimscape.data.room.FavouriteEntity
import com.swimscape.data.room.SpotDao
import com.swimscape.model.AlertUiModel
import com.swimscape.model.SpotDetailsUiModel
import com.swimscape.model.SpotSummaryUiModel
import com.swimscape.model.toAlertEntity
import com.swimscape.model.toAlertUiModel
import com.swimscape.model.toSpotDetailsUiModel
import com.swimscape.model.toSpotEntity
import com.swimscape.model.toSpotSummaryUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SwimRepository(
    private val spotDao: SpotDao,
    private val favouriteDao: FavouriteDao,
    private val alertDao: AlertDao,
    private val firestoreDataSource: FirestoreDataSource
) {
    private val _isOfflineSampleData = MutableStateFlow(false)
    val isOfflineSampleData: StateFlow<Boolean> = _isOfflineSampleData

    val spotsFlow: Flow<List<SpotSummaryUiModel>> = spotDao.observeSpots()
        .map { spots -> spots.map { it.toSpotSummaryUiModel() } }

    fun observeSpotDetails(spotId: String): Flow<SpotDetailsUiModel?> = combine(
        spotDao.observeSpot(spotId),
        favouriteDao.observeFavouriteSpotIds().map { it.contains(spotId) },
        alertDao.observeAlertsForSpotIds(listOf(spotId)).map { alerts ->
            alerts.maxByOrNull { it.timestampEpoch }?.message
        }
    ) { spot, isFav, latestAlert ->
        spot?.toSpotDetailsUiModel(
            latestAlertSnippet = latestAlert,
            isFavourite = isFav
        )
    }

    fun observeFavourites(): Flow<List<SpotSummaryUiModel>> =
        favouriteDao.observeFavouriteSpotSummaries()
            .map { spots -> spots.map { it.toSpotSummaryUiModel() } }

    fun observeAlerts(): Flow<List<AlertUiModel>> =
        favouriteDao.observeFavouriteSpotIds().flatMapLatest { spotIds ->
            if (spotIds.isEmpty()) flowOf(emptyList())
            else combine(
                alertDao.observeAlertsForSpotIds(spotIds),
                spotDao.observeSpots()
            ) { alerts, spots ->
                val spotMap = spots.associateBy { it.spotId }
                alerts.map { alert ->
                    alert.toAlertUiModel(spotMap[alert.spotId]?.name ?: alert.spotId)
                }
            }
        }

    suspend fun refreshSpotsFromFirestore() = withContext(Dispatchers.IO) {
        try {
            val spots = firestoreDataSource.fetchSpots()
            if (spots.isNotEmpty()) {
                spotDao.upsertSpots(spots.map { it.toSpotEntity() })
                _isOfflineSampleData.value = false
            } else {
                upsertSampleSpots()
                _isOfflineSampleData.value = true
            }
        } catch (e: Exception) {
            upsertSampleSpots()
            _isOfflineSampleData.value = true
        }
    }

    private suspend fun upsertSampleSpots() {
        val sampleSpots = listOf(
            com.swimscape.data.room.SpotEntity(
                spotId = "sample1",
                name = "Lough Neagh",
                county = "Antrim",
                lat = 54.6,
                lng = -6.4,
                notes = "Sample spot - add data to Firestore",
                waterTempC = 12.0,
                riskStatus = "Low",
                lastUpdatedEpoch = System.currentTimeMillis()
            ),
            com.swimscape.data.room.SpotEntity(
                spotId = "sample2",
                name = "Portrush East Strand",
                county = "Antrim",
                lat = 55.2,
                lng = -6.65,
                notes = "Sample spot - add data to Firestore",
                waterTempC = 14.0,
                riskStatus = "Moderate",
                lastUpdatedEpoch = System.currentTimeMillis()
            )
        )
        spotDao.upsertSpots(sampleSpots)
    }

    suspend fun refreshAlertsFromFirestoreForFavourites() = withContext(Dispatchers.IO) {
        val favIds = favouriteDao.observeFavouriteSpotIds().first()
        if (favIds.isEmpty()) return@withContext
        try {
            val alerts = firestoreDataSource.fetchAlertsForSpotIds(favIds)
            alertDao.upsertAlerts(alerts.map { it.toAlertEntity() })
        } catch (e: Exception) {
            // Keep cached data on error
        }
    }

    suspend fun toggleFavourite(spotId: String) = withContext(Dispatchers.IO) {
        val isFav = favouriteDao.isFavourite(spotId)
        if (isFav) {
            favouriteDao.removeFavourite(spotId)
        } else {
            favouriteDao.insertFavourite(FavouriteEntity(spotId, System.currentTimeMillis()))
        }
    }
}
