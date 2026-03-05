package com.swimscape.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.swimscape.repository.SwimRepository

class SpotDetailsViewModelFactory(
    private val savedStateHandle: SavedStateHandle,
    private val repository: SwimRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SpotDetailsViewModel(savedStateHandle, repository) as T
    }
}
