package com.example.propdash.data.model

data class MaintenanceRequest(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: List<String>,
    var resolved: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val userId: String,
    val propertyId: String,
    val property: Property,
    val maintenanceRequestUpdates: List<MaintenanceRequestUpdate>
)