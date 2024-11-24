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
        loadMaintenanceRequests()
    }

    private fun loadMaintenanceRequests() {
        viewModelScope.launch {
            fetchAndSetMaintenanceRequests()
        }
    }

    private suspend fun fetchAndSetMaintenanceRequests(): List<MaintenanceRequest> {
        return try {
            _isLoading.value = true
            val result = maintenanceRequestRepository.getMaintenanceRequests(userSession.cookie)
            if (result.isSuccessful) {
                val requests = result.body()!!
                _maintenanceRequests.value = requests
                requests
            } else {
                _maintenanceError.value = result.errorBody()?.string()
                emptyList()
            }
        } catch (e: Exception) {
            _maintenanceError.value = e.message
            e.printStackTrace()
            emptyList()
        } finally {
            _isLoading.value = false
        }
    }

    fun applyResolvedFilter(resolved: Boolean?) {
        viewModelScope.launch {
            val requests = fetchAndSetMaintenanceRequests()
            val filteredRequests = when (resolved) {
                null -> requests
                false -> requests.filter { !it.resolved }
                true -> requests.filter { it.resolved }
            }
            _maintenanceRequests.value = filteredRequests
        }
    }



    fun onPullToRefreshTrigger() {
        _isRefreshing.update { true }
        viewModelScope.launch {
            _maintenanceError.value = null
            loadMaintenanceRequests()
            _isRefreshing.update { false }
        }
    }
}