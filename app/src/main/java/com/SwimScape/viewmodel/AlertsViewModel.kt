package com.swimscape.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swimscape.model.AlertUiModel
import com.swimscape.repository.SwimRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class AlertsUiState(
    val alerts: List<AlertUiModel> = emptyList(),
    val isLoading: Boolean = false
)

class AlertsViewModel(
    private val repository: SwimRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlertsUiState())
    val uiState: StateFlow<AlertsUiState> = _uiState.asStateFlow()

    init {
        repository.observeAlerts().onEach { alerts ->
            _uiState.value = _uiState.value.copy(alerts = alerts)
        }.launchIn(viewModelScope)

        refreshAlerts()
    }

    fun refreshAlerts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.refreshAlertsFromFirestoreForFavourites()
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}
