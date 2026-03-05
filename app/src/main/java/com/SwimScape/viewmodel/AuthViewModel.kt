package com.swimscape.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow(AuthUiState(isLoggedIn = auth.currentUser != null))
    val authState: StateFlow<AuthUiState> = _authState.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _authState.value = _authState.value.copy(isLoggedIn = firebaseAuth.currentUser != null)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            try {
                withContext(Dispatchers.IO) {
                    auth.signInWithEmailAndPassword(email, password).await()
                }
                _authState.value = _authState.value.copy(isLoading = false, isLoggedIn = true)
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Login failed"
                )
            }
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            if (password != confirmPassword) {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = "Passwords do not match"
                )
                return@launch
            }
            try {
                withContext(Dispatchers.IO) {
                    auth.createUserWithEmailAndPassword(email, password).await()
                }
                _authState.value = _authState.value.copy(isLoading = false, isLoggedIn = true)
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Registration failed"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                auth.signOut()
            }
        }
    }

    fun clearError() {
        _authState.value = _authState.value.copy(error = null)
    }
}
