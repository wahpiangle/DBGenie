package com.example.propdash.data.repository

import com.example.propdash.data.model.CreateProperty
import com.example.propdash.data.model.Property
import com.example.propdash.data.service.ApiClient
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Response

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
        val nameBody = name.toRequestBody(MultipartBody.FORM)
        val descriptionBody = description.toRequestBody(MultipartBody.FORM)
        val userIdBody = userId.toRequestBody(MultipartBody.FORM)
        return apiService.createProperty(
            cookie,
            nameBody,
            descriptionBody,
            userIdBody,
            imageUrl
        )
    }

    suspend fun updateProperty(id: String, cookie: String, property: CreateProperty): Response<Property> {
        return apiService.updateProperty(
            cookie=cookie,
            id=id,
            name=property.name.toRequestBody(MultipartBody.FORM),
            description=property.description.toRequestBody(MultipartBody.FORM),
            userId=property.userId.toRequestBody(MultipartBody.FORM),
            updateImage = property.updateImage.toString().toRequestBody(MultipartBody.FORM),
            files=property.imageUrl
        )
    }

    suspend fun removeProperty(cookie: String, id: String): Response<ResponseBody> {
        return apiService.removeProperty(cookie, id)
    }
}