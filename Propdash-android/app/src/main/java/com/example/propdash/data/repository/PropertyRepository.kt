package com.example.propdash.data.repository

import com.example.propdash.data.model.CreateProperty
import com.example.propdash.data.model.Property
import com.example.propdash.data.service.ApiClient
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Part

class PropertyRepository {
    private val apiService = ApiClient.apiService

    suspend fun getPropertiesByUser(cookie: String): Response<List<Property>> {
        return apiService.getPropertiesByUser(
            cookie
        )
    }

    suspend fun getProperty(cookie: String, id: String): Response<Property> {
        return apiService.getProperty(
            cookie,
            id
        )
    }

    suspend fun createProperty(
        cookie: String,
        name: String,
        description: String,
        userId: String,
        imageUrl: List<MultipartBody.Part>
    ): Response<Property> {
        return apiService.createProperty(
            cookie,
            name,
            description,
            userId,
            imageUrl
        )
    }

    suspend fun updateProperty(id: String, cookie: String, property: Property): Response<Property> {
        return apiService.updateProperty(cookie, id, property)
    }

    suspend fun removeProperty(cookie: String, id: String): Response<ResponseBody> {
        return apiService.removeProperty(cookie, id)
    }
}