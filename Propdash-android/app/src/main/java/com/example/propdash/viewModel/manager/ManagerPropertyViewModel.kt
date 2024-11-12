package com.example.propdash.viewModel.manager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propdash.data.model.Property
import com.example.propdash.data.model.User
import com.example.propdash.data.repository.PropertyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManagerPropertyViewModel(
    private val userSession: User,
): ViewModel() {
    private val _properties = MutableStateFlow<List<Property>>(emptyList())
    private val _fetchPropertyError = MutableStateFlow<String?>(null)
    val fetchPropertyError = _fetchPropertyError.asStateFlow()
    private val propertyRepository = PropertyRepository()
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()
    val properties = _properties.asStateFlow()

    init {
        fetchPropertyData()
    }
    private fun fetchPropertyData() {
        viewModelScope.launch {
            try {
                val result = propertyRepository.getPropertiesByUser(userSession.cookie)
                _properties.value = result.body()!!
                Log.d("ManagerViewModel", "fetchPropertyData: ${_properties.value}")

            } catch (e: Exception) {
                _fetchPropertyError.value = e.message
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
}