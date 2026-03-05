package com.swimscape.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.swimscape.repository.SwimRepository
import com.swimscape.viewmodel.SpotDetailsViewModel
import com.swimscape.viewmodel.SpotDetailsViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotDetailsScreen(
    spotId: String,
    savedStateHandle: SavedStateHandle,
    repository: SwimRepository,
    onNavigateBack: () -> Unit,
    viewModel: SpotDetailsViewModel = viewModel(
        factory = SpotDetailsViewModelFactory(savedStateHandle, repository)
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.details?.name ?: "Spot Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←")
                    }
                },
                actions = {
                    uiState.details?.let { details ->
                        IconButton(onClick = { viewModel.toggleFavourite() }) {
                            Text(if (details.isFavourite) "★" else "☆")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading && uiState.details == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            uiState.details?.let { details ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = details.name,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = details.county,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            details.lat?.let { lat ->
                                details.lng?.let { lng ->
                                    Text(
                                        text = "Lat: $lat, Lng: $lng",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                            details.waterTempC?.let { temp ->
                                Text(
                                    text = "Water temp: ${temp}°C",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            details.riskStatus?.let { status ->
                                Text(
                                    text = "Risk: $status",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            details.lastUpdated?.let { epoch ->
                                Text(
                                    text = "Last updated: ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date(epoch))}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            details.latestAlertSnippet?.let { snippet ->
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Latest alert: $snippet",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            details.notes?.let { notes ->
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Notes: $notes",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
