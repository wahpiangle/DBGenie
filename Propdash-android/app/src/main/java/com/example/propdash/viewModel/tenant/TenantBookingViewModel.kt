package com.example.propdash.viewModel.tenant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propdash.data.model.Booking
import com.example.propdash.data.model.User
import com.example.propdash.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TenantBookingViewModel(
    private val userSession: User,
):ViewModel() {
    private val bookingRepository = BookingRepository()

    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    private val _bookingError = MutableStateFlow<String?>(null)
    private val _isRefreshing = MutableStateFlow(false)
    val bookings = _bookings.asStateFlow()
    val bookingError = _bookingError.asStateFlow()
    val isRefreshing = _isRefreshing.asStateFlow()

    init{
        getBookings()
    }

    private fun getBookings(){
        viewModelScope.launch {
            try {
                val result = bookingRepository.getBookings(
                    userSession.cookie
                )
                if(result.isSuccessful){
                    _bookings.value = result.body()!!
                }else{
                    _bookingError.value = result.errorBody()?.string()
                }
            } catch (e: Exception) {
                _bookingError.value = e.message
                e.printStackTrace()
            }
        }
    }

    fun onPullToRefreshTrigger() {
        _isRefreshing.update { true }
        viewModelScope.launch {
            getBookings()
            _isRefreshing.update { false }
        }
    }
}