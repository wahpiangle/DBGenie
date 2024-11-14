package com.example.propdash.viewModel.manager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propdash.components.manager.ManagerScreen
import com.example.propdash.data.model.Property
import com.example.propdash.data.model.User
import com.example.propdash.data.repository.PropertyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManagerPropertyDetailViewModel(
    val userSession: User,
    private val propertyId: String,
    private val navigate: (String) -> Unit
): ViewModel() {
    private val propertyRepository = PropertyRepository()
    private val _isLoading = MutableStateFlow<Boolean>(false)
    private val _property = MutableStateFlow<Property?>(null)
    private val _error = MutableStateFlow<String?>(null)
    private val _isRefreshing = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    val isRefreshing = _isRefreshing.asStateFlow()
    val property = _property.asStateFlow()
    val error = _error.asStateFlow()

    init{
        fetchPropertyData()
    }

    private fun fetchPropertyData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = propertyRepository.getProperty(userSession.cookie, propertyId)
                _property.value = result.body()!!
                Log.d("ManagerViewModel", result.body().toString())

            } catch (e: Exception) {
                _error.value = e.message
                Log.e("ManagerViewModel", e.message!!)
            }
            _isLoading.value = false
        }
    }
    fun deleteProperty() {
        viewModelScope.launch {
            try {
                propertyRepository.removeProperty(userSession.cookie, propertyId)
                navigate(ManagerScreen.ManagerPropertyScreen.route)
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("ManagerViewModel", e.message!!)
            }
        }
    }

    fun onPullToRefreshTrigger() {
        _isRefreshing.update { true }
        viewModelScope.launch {
            fetchPropertyData()
            _isRefreshing.update { false }
        }
    }

    data class PropertyScreenState(
        val items: Property? = null,
        val isRefreshing: Boolean = false
    )
}