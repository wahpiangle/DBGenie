package com.example.propdash.viewModel.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propdash.data.model.MaintenanceRequest
import com.example.propdash.data.model.User
import com.example.propdash.data.repository.MaintenanceRequestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManagerMaintenanceRequestViewModel(
    private val userSession: User,
    private val navigate: (String) -> Unit
) : ViewModel() {
    private val maintenanceRequestRepository = MaintenanceRequestRepository()
    private val _maintenanceRequests = MutableStateFlow<List<MaintenanceRequest>>(emptyList())
    private val _maintenanceError = MutableStateFlow<String?>(null)
    private val _isRefreshing = MutableStateFlow(false)
    private val _isLoading = MutableStateFlow(false)
    val maintenanceRequests = _maintenanceRequests
    val maintenanceError = _maintenanceError
    val isRefreshing = _isRefreshing
    val isLoading = _isLoading

    init {
        getMaintenanceRequests()
    }

    private fun getMaintenanceRequests() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = maintenanceRequestRepository.getMaintenanceRequests(
                    userSession.cookie
                )
                if (result.isSuccessful) {
                    _maintenanceRequests.value = result.body()!!
                } else {
                    _maintenanceError.value = result.errorBody()?.string()
                }
            } catch (e: Exception) {
                _maintenanceError.value = e.message
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onPullToRefreshTrigger() {
        _isRefreshing.update { true }
        viewModelScope.launch {
            _maintenanceError.value = null
            getMaintenanceRequests()
            _isRefreshing.update { false }
        }
    }
}