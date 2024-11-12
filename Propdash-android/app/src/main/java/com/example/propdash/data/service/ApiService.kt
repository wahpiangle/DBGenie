package com.example.propdash.data.service

import com.example.propdash.data.model.CreateProperty
import com.example.propdash.data.model.LoginRequest
import com.example.propdash.data.model.Property
import com.example.propdash.data.model.RegisterRequest
import com.example.propdash.data.model.User
import com.example.propdash.data.model.VerificationRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

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

    @GET("/properties")
    suspend fun getPropertiesByUser(
        @Header("Cookie") cookie: String,
    ): Response<List<Property>>

    @Multipart
    @POST("/properties")
    suspend fun createProperty(
        @Header("Cookie") cookie: String,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("rentalPerMonth") rentalPerMonth: RequestBody,
        @Part("userId") userId: RequestBody,
        @Part files: List<MultipartBody.Part>
    ): Response<Property>

    @GET("/properties/{id}")
    suspend fun getProperty(
        @Header("Cookie") cookie: String,
        @Path("id") id: String,
    ): Response<Property>

    @DELETE("/properties/{id}")
    suspend fun removeProperty(
        @Header("Cookie") cookie: String,
        @Path("id") id: String,
    ): Response<ResponseBody>

    @PATCH("/properties/{id}")
    suspend fun updateProperty(
        @Header("Cookie") cookie: String,
        @Path("id") id: String,
        @Body property: Property,
    ): Response<Property>
}

// Singleton object for Retrofit
object ApiClient {
    private const val BASE_URL = "http:///"
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}