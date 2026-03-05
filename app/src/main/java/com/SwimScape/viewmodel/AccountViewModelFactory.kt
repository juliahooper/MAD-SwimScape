package com.swimscape.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.swimscape.repository.SwimRepository

class AccountViewModelFactory(
    private val repository: SwimRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AccountViewModel(repository) as T
    }
}
