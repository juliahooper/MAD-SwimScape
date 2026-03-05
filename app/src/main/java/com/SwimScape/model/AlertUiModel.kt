package com.swimscape.model

data class AlertUiModel(
    val alertId: String,
    val spotId: String,
    val spotName: String,
    val title: String,
    val message: String,
    val severity: String,
    val timestampEpoch: Long
)
