package com.example.propdash.viewModel.tenant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propdash.data.model.ErrorResponse
import com.example.propdash.data.model.MaintenanceRequest
import com.example.propdash.data.model.User
import com.example.propdash.data.repository.MaintenanceRequestRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TenantMaintenanceDetailViewModel (
    private val maintenanceId : String,
    private val userSession: User
):ViewModel() {
    private val maintenanceRepository = MaintenanceRequestRepository()
    private val _maintenanceRequest = MutableStateFlow<MaintenanceRequest?>(null)
    private val _maintenanceRequestError = MutableStateFlow<String?>(null)
    private val _isRefreshing = MutableStateFlow(false)
    val maintenanceRequest = _maintenanceRequest.asStateFlow()
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        getMaintenanceRequest()
    }

    private fun getMaintenanceRequest(){
        viewModelScope.launch {
            try {
                val result = maintenanceRepository.getMaintenanceRequest(
                    userSession.cookie,
                    maintenanceId
                )
                if(result.isSuccessful){
                    _maintenanceRequest.value = result.body()!!
                }else{
                    _maintenanceRequestError.value = Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java).error
                }
            } catch (e: Exception) {
                _maintenanceRequestError.value = e.message
                e.printStackTrace()
            }
        }
    }

    fun onPullToRefreshTrigger() {
        _isRefreshing.update { true }
        viewModelScope.launch {
            getMaintenanceRequest()
            _isRefreshing.update { false }
        }
    }

}