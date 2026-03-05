package com.swimscape.model

data class SpotDetailsUiModel(
    val spotId: String,
    val name: String,
    val county: String,
    val lat: Double?,
    val lng: Double?,
    val waterTempC: Double?,
    val riskStatus: String?,
    val lastUpdated: Long?,
    val latestAlertSnippet: String?,
    val notes: String?,
    val isFavourite: Boolean
)
