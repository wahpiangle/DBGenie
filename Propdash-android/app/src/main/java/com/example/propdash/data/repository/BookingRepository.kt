package com.example.propdash.data.repository

import com.example.propdash.data.model.Booking
import com.example.propdash.data.model.CreateBooking
import com.example.propdash.data.service.ApiClient
import retrofit2.Response

class BookingRepository {
    private val apiService = ApiClient.apiService

    suspend fun createBooking(
        cookie: String,
        tenantEmail: String,
        remarks: String,
        checkIn: String,
        checkOut: String,
        rentalPrice: String,
        rentCollectionDay: Int,
        propertyId: String
    ): Response<Booking>
    {
        return apiService.createBooking(
            cookie,
            CreateBooking(
                tenantEmail,
                remarks,
                checkIn,
                checkOut,
                rentalPrice,
                rentCollectionDay,
                propertyId
            )
        )
    }
}