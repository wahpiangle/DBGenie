package com.example.propdash.data.repository

import com.example.propdash.data.model.RegisterRequest
import com.example.propdash.data.model.User
import com.example.propdash.data.service.ApiClient

class UserRepository {
    private val apiService = ApiClient.apiService

    suspend fun getUsers(): List<User> {
        return apiService.getUsers()
    }

    suspend fun register(user: RegisterRequest): User {
        return apiService.register(user)
    }
}
