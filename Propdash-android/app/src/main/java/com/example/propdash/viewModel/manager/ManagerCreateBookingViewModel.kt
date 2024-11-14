package com.example.propdash.viewModel.manager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propdash.components.manager.ManagerScreen
import com.example.propdash.data.model.ErrorResponse
import com.example.propdash.data.model.User
import com.example.propdash.data.repository.BookingRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ManagerCreateBookingViewModel (
    private val userSession: User,
    private val navigate: (String) -> Unit
):ViewModel(){
    private val bookingRepository = BookingRepository()
    private val _createBookingError = MutableStateFlow<String?>(null)
    private val _deleteBookingError = MutableStateFlow<String?>(null)
    var createBookingError = _createBookingError.asStateFlow()
    var deleteBookingError = _deleteBookingError.asStateFlow()

    fun createBooking(
        tenantEmail: String,
        remarks: String,
        checkIn: Long,
        checkOut: Long,
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
                if(result.isSuccessful){
                    navigate(ManagerScreen.ManagerPropertyDetailScreen.createRoute(
                        propertyId
                    ))
                }else{
                    val errorResponse = Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java)
                    _createBookingError.value = errorResponse.error
                }
            } catch (e: Exception) {
                _createBookingError.value = e.message
                Log.e("ManagerViewModel", e.message!!)
            }
        }
    }

    fun deleteBooking(
        bookingId: String
    ){
        viewModelScope.launch {
            try {
                val result = bookingRepository.deleteBooking(
                    userSession.cookie,
                    bookingId
                )
                if(result.isSuccessful){
                    navigate(ManagerScreen.ManagerPropertyDetailScreen.createRoute(
                        bookingId
                    ))
                }else{
                    val errorResponse = Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java)
                    _deleteBookingError.value = errorResponse.error
                }
            } catch (e: Exception) {
                _deleteBookingError.value = e.message
                Log.e("ManagerViewModel", e.message!!)
            }
        }
    }
}