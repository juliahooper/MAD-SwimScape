package com.swimscape.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swimscape.model.SpotSummaryUiModel
import com.swimscape.repository.SwimRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class FavouritesUiState(
    val favourites: List<SpotSummaryUiModel> = emptyList()
)

class FavouritesViewModel(
    private val repository: SwimRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavouritesUiState())
    val uiState: StateFlow<FavouritesUiState> = _uiState.asStateFlow()

    init {
        repository.observeFavourites().onEach { favourites ->
            _uiState.value = FavouritesUiState(favourites = favourites)
        }.launchIn(viewModelScope)
    }
}
