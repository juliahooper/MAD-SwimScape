package com.swimscape.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swimscape.model.SpotDetailsUiModel
import com.swimscape.repository.SwimRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class SpotDetailsUiState(
    val details: SpotDetailsUiModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class SpotDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: SwimRepository
) : ViewModel() {

    private val spotId: String = savedStateHandle.get<String>("spotId") ?: ""

    private val _uiState = MutableStateFlow(SpotDetailsUiState())
    val uiState: StateFlow<SpotDetailsUiState> = _uiState.asStateFlow()

    init {
        repository.observeSpotDetails(spotId).onEach { details ->
            _uiState.value = _uiState.value.copy(
                details = details,
                isLoading = details == null && spotId.isNotEmpty()
            )
        }.launchIn(viewModelScope)
    }

    fun toggleFavourite() {
        viewModelScope.launch {
            repository.toggleFavourite(spotId)
        }
    }
}
