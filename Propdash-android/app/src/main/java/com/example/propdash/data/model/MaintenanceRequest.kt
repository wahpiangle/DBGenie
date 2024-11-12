package com.example.propdash.data.model

data class MaintenanceRequest(
    val id: String,
    val description: String,
    val createdAt: String,
    val updatedAt: String,
    val userId: String,
    val propertyId: String
)