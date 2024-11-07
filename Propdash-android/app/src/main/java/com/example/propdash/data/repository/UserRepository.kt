package com.example.propdash.data.repository

import com.example.propdash.data.model.LoginRequest
import com.example.propdash.data.model.RegisterRequest
import com.example.propdash.data.model.User
import com.example.propdash.data.model.VerificationRequest
import com.example.propdash.data.service.ApiClient
import okhttp3.ResponseBody
import retrofit2.Response

class UserRepository {
    private val apiService = ApiClient.apiService

    suspend fun getUsers(): List<User> {
        return apiService.getUsers()
    }

    suspend fun register(user: RegisterRequest): Response<User> {
        return apiService.register(user)
    }

    suspend fun login(user: LoginRequest): Response<User> {
        return apiService.login(user)
    }

    suspend fun verifyAccount(cookie: String, token: VerificationRequest): Response<ResponseBody> {
        return apiService.verifyAccount(cookie, token)
    }
}
