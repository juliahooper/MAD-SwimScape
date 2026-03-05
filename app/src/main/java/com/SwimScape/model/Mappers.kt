package com.swimscape.model

import com.swimscape.data.room.AlertEntity
import com.swimscape.data.room.SpotEntity

fun SpotEntity.toSpotSummaryUiModel(): SpotSummaryUiModel = SpotSummaryUiModel(
    spotId = spotId,
    name = name,
    county = county,
    riskStatus = riskStatus,
    waterTempC = waterTempC
)

fun SpotEntity.toSpotDetailsUiModel(
    latestAlertSnippet: String?,
    isFavourite: Boolean
): SpotDetailsUiModel = SpotDetailsUiModel(
    spotId = spotId,
    name = name,
    county = county,
    lat = lat,
    lng = lng,
    waterTempC = waterTempC,
    riskStatus = riskStatus,
    lastUpdated = lastUpdatedEpoch,
    latestAlertSnippet = latestAlertSnippet,
    notes = notes,
    isFavourite = isFavourite
)

fun FirestoreSpotDto.toSpotEntity(): SpotEntity = SpotEntity(
    spotId = spotId,
    name = name,
    county = county,
    lat = lat,
    lng = lng,
    notes = notes,
    waterTempC = waterTempC,
    riskStatus = riskStatus,
    lastUpdatedEpoch = lastUpdated
)

fun FirestoreAlertDto.toAlertEntity(): AlertEntity = AlertEntity(
    alertId = alertId,
    spotId = spotId,
    title = title,
    message = message,
    severity = severity,
    timestampEpoch = timestampEpoch
)

fun AlertEntity.toAlertUiModel(spotName: String): AlertUiModel = AlertUiModel(
    alertId = alertId,
    spotId = spotId,
    spotName = spotName,
    title = title,
    message = message,
    severity = severity,
    timestampEpoch = timestampEpoch
)
