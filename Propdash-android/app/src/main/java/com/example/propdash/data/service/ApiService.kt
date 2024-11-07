package com.example.propdash.data.service

import com.example.propdash.data.model.LoginRequest
import com.example.propdash.data.model.RegisterRequest
import com.example.propdash.data.model.User
import com.example.propdash.data.model.VerificationRequest
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @GET("/users")
    suspend fun getUsers(): List<User>

    @POST("/auth/register")
    suspend fun register(@Body user: RegisterRequest): Response<User>

    @POST("/auth/login")
    suspend fun login(@Body user: LoginRequest): Response<User>

    @POST("/auth/verify-account")
    suspend fun verifyAccount(
        @Header("Cookie") cookie: String,
        @Body token: VerificationRequest): Response<ResponseBody>
}

// Singleton object for Retrofit
object ApiClient {
    private const val BASE_URL = "http://10.163.13.69:8080/"
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}