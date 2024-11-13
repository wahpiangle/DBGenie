package com.example.propdash.viewModel.manager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propdash.components.manager.ManagerScreen
import com.example.propdash.data.model.User
import com.example.propdash.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ManagerCreateBookingViewModel (
    private val userSession: User,
    private val navigate: (String) -> Unit
):ViewModel(){
    private val bookingRepository = BookingRepository()
    private val _createBookingError = MutableStateFlow<String?>(null)
    var createBookingError = _createBookingError.asStateFlow()

    fun createBooking(
        tenantEmail: String,
        remarks: String,
        checkIn: String,
        checkOut: String,
        rentalPrice: String,
        rentCollectionDay: Int,
        propertyId: String
    ){
        viewModelScope.launch {
            try {
                val result = bookingRepository.createBooking(
                    userSession.cookie,
                    tenantEmail,
                    remarks,
                    checkIn,
                    checkOut,
                    rentalPrice,
                    rentCollectionDay,
                    propertyId
                )
            } catch (e: Exception) {
                _createBookingError.value = e.message
                Log.e("ManagerViewModel", e.message!!)
            }
        }
    }
}