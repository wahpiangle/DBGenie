package com.example.propdash.data.model

import java.io.File

data class Property(
    val id: String,
    val name: String,
    val description: String,
    val rentalPerMonth: String,
    val imageUrl: List<String>,
    val createdAt: String,
    val updatedAt: String,
    val userId: String,
    val booking: List<Booking>
)

data class CreateProperty(
    val name: String,
    val description: String,
    val userId: String,
    val imageUrl: List<String>
)