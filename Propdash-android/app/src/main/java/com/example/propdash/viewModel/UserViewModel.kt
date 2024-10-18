package com.example.propdash.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propdash.data.model.User
import com.example.propdash.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val repository = UserRepository()

    // StateFlow to hold the list of users
    private val _userState = MutableStateFlow<List<User>>(emptyList())
    val userState: StateFlow<List<User>> = _userState

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Fetch users from API
    fun fetchUsers() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val users = repository.getUsers()
                _userState.value = users
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error fetching users", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
