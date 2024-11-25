package com.example.propdash.data.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: Role,
    val verified: Boolean,
    var cookie: String,
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val role: Role
)

data class LoginRequest(
    val email: String,
    val password: String
)

enum class Role {
    TENANT,
    MANAGER
}