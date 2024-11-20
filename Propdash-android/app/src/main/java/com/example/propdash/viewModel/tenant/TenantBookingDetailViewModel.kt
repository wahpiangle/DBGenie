package com.example.propdash.viewModel.tenant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propdash.data.model.Booking
import com.example.propdash.data.model.ErrorResponse
import com.example.propdash.data.model.User
import com.example.propdash.data.repository.BookingRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TenantBookingDetailViewModel (
    private val userSession: User,
    private val bookingId : String
):ViewModel(){
    private val bookingRepository = BookingRepository()
    private val _booking = MutableStateFlow<Booking?>(null)
    private val _bookingError = MutableStateFlow<String?>(null)
    val booking = _booking.asStateFlow()
    val bookingError = _bookingError.asStateFlow()

    init{
        getBookingDetail()
    }

    private fun getBookingDetail(){
        viewModelScope.launch {
            try {
                val result = bookingRepository.getBooking(
                    userSession.cookie,
                    bookingId
                )
                if(result.isSuccessful){
                    _booking.value = result.body()!!
                }else{
                    _bookingError.value = Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java).error
                }
            } catch (e: Exception) {
                _bookingError.value = e.message
                e.printStackTrace()
            }
        }
    }
}
