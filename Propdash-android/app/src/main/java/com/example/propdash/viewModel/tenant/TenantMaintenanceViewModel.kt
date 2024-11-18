package com.example.propdash.viewModel.tenant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propdash.data.model.MaintenanceRequest
import com.example.propdash.data.model.User
import com.example.propdash.data.repository.MaintenanceRequestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TenantMaintenanceViewModel(
    private val userSession: User
) : ViewModel() {

    private val maintenanceRequestRepository = MaintenanceRequestRepository()
    private val _maintenanceRequests = MutableStateFlow<List<MaintenanceRequest>>(emptyList())
    private val _maintenanceError = MutableStateFlow<String?>(null)
    private val _isRefreshing = MutableStateFlow(false)
    val maintenanceRequests = _maintenanceRequests.asStateFlow()
    val maintenanceError = _maintenanceError.asStateFlow()
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        getMaintenanceRequests()
    }

    private fun getMaintenanceRequests() {
        viewModelScope.launch {
            try {
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