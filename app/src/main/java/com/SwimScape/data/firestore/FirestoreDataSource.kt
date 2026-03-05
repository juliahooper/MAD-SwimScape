package com.swimscape.data.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.swimscape.model.FirestoreSpotDto
import com.swimscape.model.FirestoreAlertDto
import kotlinx.coroutines.tasks.await

/**
 * Firestore data source for fetching spots and alerts.
 * All operations are suspend functions for use with coroutines.
 */
class FirestoreDataSource {

    private val firestore = FirebaseFirestore.getInstance()
    private val spotsCollection = firestore.collection("spots")
    private val alertsCollection = firestore.collection("alerts")

    suspend fun fetchSpots(): List<FirestoreSpotDto> {
        return try {
            val snapshot = spotsCollection.get().await()
            snapshot.documents.mapNotNull { doc ->
                try {
                    val data = doc.data ?: return@mapNotNull null
                    val safetySnapshot = data["safetySnapshot"] as? Map<String, Any?>
                    FirestoreSpotDto(
                        spotId = doc.id,
                        name = (data["name"] as? String) ?: "",
                        county = (data["county"] as? String) ?: "",
                        lat = (data["lat"] as? Number)?.toDouble(),
                        lng = (data["lng"] as? Number)?.toDouble(),
                        notes = data["notes"] as? String,
                        waterTempC = (safetySnapshot?.get("waterTempC") as? Number)?.toDouble(),
                        riskStatus = safetySnapshot?.get("riskStatus") as? String,
                        lastUpdated = (safetySnapshot?.get("lastUpdated") as? com.google.firebase.Timestamp)?.toDate()?.time
                    )
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun fetchAlertsForSpotIds(spotIds: List<String>): List<FirestoreAlertDto> {
        if (spotIds.isEmpty()) return emptyList()
        return try {
            val snapshot = alertsCollection
                .whereIn("spotId", spotIds.take(10)) // Firestore 'in' limited to 10
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.mapNotNull { doc ->
                try {
                    val data = doc.data ?: return@mapNotNull null
                    FirestoreAlertDto(
                        alertId = doc.id,
                        spotId = (data["spotId"] as? String) ?: "",
                        title = (data["title"] as? String) ?: "",
                        message = (data["message"] as? String) ?: "",
                        severity = (data["severity"] as? String) ?: "info",
                        timestampEpoch = (data["timestamp"] as? com.google.firebase.Timestamp)?.toDate()?.time ?: 0L
                    )
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
