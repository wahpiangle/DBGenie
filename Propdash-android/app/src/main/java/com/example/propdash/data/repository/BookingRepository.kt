package com.example.propdash.data.repository

import com.example.propdash.data.model.Booking
import com.example.propdash.data.model.CreateBooking
import com.example.propdash.data.model.GeneralResponse
import com.example.propdash.data.service.ApiClient
import retrofit2.Response

class BookingRepository {
    private val apiService = ApiClient.apiService

    suspend fun createBooking(
        cookie: String,
        tenantEmail: String,
        remarks: String,
        checkIn: Long,
        checkOut: Long,
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

    suspend fun deleteBooking(
        cookie: String,
        bookingId: String
    ): Response<GeneralResponse>
    {
        return apiService.deleteBooking(
            cookie,
            bookingId
        )
    }
}