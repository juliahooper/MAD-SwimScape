package com.swimscape.model

data class SpotSummaryUiModel(
    val spotId: String,
    val name: String,
    val county: String,
    val riskStatus: String?,
    val waterTempC: Double?
)
