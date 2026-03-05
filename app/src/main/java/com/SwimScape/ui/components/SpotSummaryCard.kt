package com.swimscape.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swimscape.model.SpotSummaryUiModel

@Composable
fun SpotSummaryCard(
    spot: SpotSummaryUiModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = spot.name,
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = spot.county,
                    style = MaterialTheme.typography.bodyMedium
                )
                spot.riskStatus?.let { status ->
                    Text(
                        text = status,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            spot.waterTempC?.let { temp ->
                Text(
                    text = "${temp}°C",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
