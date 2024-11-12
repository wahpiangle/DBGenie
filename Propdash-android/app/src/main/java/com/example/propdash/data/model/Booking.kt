package com.example.propdash.data.model

data class Booking(
    val id: String,
    val propertyId: String,
    val userId: String, //tenant
    val remarks: String?,
    val checkIn: String,
    val checkOut: String,
    val rentalPrice: String,
    val rentCollectionDay: Int,
    val createdAt: String,
    val updatedAt: String,
    val property: Property,
    val user: User,
)

enum class BookingStatus {
    VACANT,
    OCCUPIED
}