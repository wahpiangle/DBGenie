package com.example.propdash.data.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: Role,
    val verified: Boolean,
    val cookie: String
)