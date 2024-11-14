package com.example.propdash.data.model

data class Property(
    val id: String,
    val name: String,
    val description: String,
    val rentalPerMonth: String,
    val imageUrl: List<String>,
    val createdAt: String,
    val updatedAt: String,
    val userId: String,
    val bookings: List<Booking>,
    val maintenanceRequest: List<MaintenanceRequest>
)

data class CreateProperty(
    val name: String,
    val description: String,
    val userId: String,
    val imageUrl: List<String>
)