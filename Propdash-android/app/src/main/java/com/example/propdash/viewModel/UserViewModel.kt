package com.example.propdash.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propdash.data.SessionManager
import com.example.propdash.data.model.Role
import com.example.propdash.data.model.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(private val sessionManager: SessionManager) : ViewModel() {

    // Flow to observe user session
    val userSession: StateFlow<User?> = sessionManager.userSession
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    // Save user session when login is successful
    fun saveUserSession(id: String, name: String, email: String, role: Role, verified: Boolean, cookie: String) {
        viewModelScope.launch {
            val userSession = User(id, name, email, role, verified, cookie)
            sessionManager.saveUserSession(userSession)
        }
    }

    // Clear session on logout
    fun clearSession() {
        viewModelScope.launch {
            sessionManager.clearUserSession()
        }
    }
}
