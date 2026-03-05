package com.swimscape.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.swimscape.repository.SwimRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class AccountUiState(
    val email: String? = null,
    val isLoggedIn: Boolean = false
)

class AccountViewModel(
    private val repository: SwimRepository
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(
        AccountUiState(
            email = auth.currentUser?.email,
            isLoggedIn = auth.currentUser != null
        )
    )
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _uiState.value = AccountUiState(
                email = firebaseAuth.currentUser?.email,
                isLoggedIn = firebaseAuth.currentUser != null
            )
        }
    }

    fun signOut() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                auth.signOut()
            }
        }
    }
}
