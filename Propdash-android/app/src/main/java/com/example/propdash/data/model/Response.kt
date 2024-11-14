package com.example.propdash.data.model

data class GeneralResponse(
    val message: String,
    val error: String
)

data class ErrorResponse(
    val error: String
)