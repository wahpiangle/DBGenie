package com.example.propdash.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propdash.data.SessionManager
import com.example.propdash.data.model.ErrorResponse
import com.example.propdash.data.model.LoginRequest
import com.example.propdash.data.model.Role
import com.example.propdash.data.model.User
import com.example.propdash.data.repository.UserRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.Response

class UserViewModel(private val sessionManager: SessionManager) : ViewModel() {
    private val userRepository = UserRepository()
    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> = _errorMessage

    val userSession: StateFlow<User?> = sessionManager.userSession
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun login(email: String, password: String) {
        viewModelScope.launch {
                val user = userRepository.login(LoginRequest(email, password))
                if(user.isSuccessful){
//                    TODO
                }else{
                    val errorResponse =
                        Gson().fromJson(user.errorBody()?.string(), ErrorResponse::class.java)
                    _errorMessage.value = errorResponse.error
                }
        }
    }

    // Save user session when login is successful
    fun saveUserSession(
        id: String,
        name: String,
        email: String,
        role: Role,
        verified: Boolean,
        cookie: String
    ) {
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
