package com.swimscape.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swimscape.model.SpotSummaryUiModel
import com.swimscape.repository.SwimRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class SpotsUiState(
    val spots: List<SpotSummaryUiModel> = emptyList(),
    val searchQuery: String = "",
    val filteredSpots: List<SpotSummaryUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val isOfflineSampleData: Boolean = false
)

class SpotsViewModel(
    private val repository: SwimRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SpotsUiState())
    val uiState: StateFlow<SpotsUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        combine(
            repository.spotsFlow,
            repository.isOfflineSampleData,
            _searchQuery
        ) { spots, isOffline, query ->
            val q = query.lowercase().trim()
            val filtered = if (q.isEmpty()) spots
            else spots.filter {
                it.name.lowercase().contains(q) || it.county.lowercase().contains(q)
            }
            SpotsUiState(
                spots = spots,
                searchQuery = query,
                filteredSpots = filtered,
                isOfflineSampleData = isOffline
            )
        }.onEach { _uiState.value = it }.launchIn(viewModelScope)

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.refreshSpotsFromFirestore()
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
