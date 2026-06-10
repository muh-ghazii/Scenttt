package com.contoh.scentapp.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.contoh.scentapp.data.model.AuthUiState
import com.contoh.scentapp.data.repository.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager.getInstance(application)

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onLoginEmailChange(value: String)    {
        _uiState.update { it.copy(loginEmail = value, errorMessage = null) }
    }
    fun onLoginPasswordChange(value: String) {
        _uiState.update { it.copy(loginPassword = value, errorMessage = null) }
    }
    fun toggleLoginPasswordVisibility() {
        _uiState.update { it.copy(showLoginPass = !it.showLoginPass)
        }
    }

    fun onRegisterNameChange(value: String) {
        _uiState.update { it.copy(registerName = value, errorMessage = null)
        }
    }
    fun onRegisterEmailChange(value: String) {
        _uiState.update { it.copy(registerEmail = value, errorMessage = null)
        }
    }
    fun onRegisterPasswordChange(value: String) {
        _uiState.update { it.copy(showRegisterPass = !it.showRegisterPass)
        }
    }

    fun login(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.loginEmail.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email tidak boleh kosong") }
            return
        }
        if (state.loginPassword.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Kata sandi tidak boleh kosong") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            delay(800)
            sessionManager.saveSession(email = state.loginEmail)
            _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
            onSuccess()
        }
    }

    fun register(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.registerName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Nama tidak boleh kosong") }
            return
        }
        if (state.registerEmail.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email tidak boleh kosong") }
            return
        }
        if (state.registerPassword.length < 6) {
            _uiState.update { it.copy(errorMessage = "Kata sandi minimal 6 karakter") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            delay(800)
            sessionManager.saveSession(email = state.registerEmail, name = state.registerName)
            _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
            onSuccess()
        }
    }

    fun logout() {
        sessionManager.clearSession()
        _uiState.update { AuthUiState() }
    }
    fun clearError() { _uiState.update { it.copy(errorMessage = null) } }
}

class AuthViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
