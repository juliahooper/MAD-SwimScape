package com.swimscape.model

data class FirestoreSpotDto(
    val spotId: String,
    val name: String,
    val county: String,
    val lat: Double?,
    val lng: Double?,
    val notes: String?,
    val waterTempC: Double?,
    val riskStatus: String?,
    val lastUpdated: Long?
)
