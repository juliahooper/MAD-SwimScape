package com.swimscape.model

data class FirestoreAlertDto(
    val alertId: String,
    val spotId: String,
    val title: String,
    val message: String,
    val severity: String,
    val timestampEpoch: Long
)
