package com.example.propdash.data.service

import com.example.propdash.data.model.User
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ApiService {
    @GET("/users")
    suspend fun getUsers(): List<User>

    @GET("/")
    suspend fun test(): Any
}

// Singleton object for Retrofit
object ApiClient {
    private const val BASE_URL = "http://192.168.1.110:8080/"
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
