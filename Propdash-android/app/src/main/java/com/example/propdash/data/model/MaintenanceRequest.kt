package com.example.propdash.data.model

data class MaintenanceRequest(
    val id: String,
    val description: String,
    val imageUrl: List<String>,
    val resolved: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val userId: String,
    val propertyId: String,
    val property: Property
)