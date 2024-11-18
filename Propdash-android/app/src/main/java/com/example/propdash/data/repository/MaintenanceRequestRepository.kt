package com.example.propdash.data.repository

import com.example.propdash.data.model.MaintenanceRequest
import com.example.propdash.data.service.ApiClient
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class MaintenanceRequestRepository {
    private val apiService = ApiClient.apiService

    suspend fun getMaintenanceRequests(cookie: String): Response<List<MaintenanceRequest>> {
        return apiService.getMaintenanceRequests(cookie)
    }

    suspend fun createMaintenanceRequest(
        cookie: String,
        propertyId: String,
        description: String,
        files: List<MultipartBody.Part>
    ): Response<MaintenanceRequest> {
        return apiService.createMaintenanceRequest(
            cookie = cookie,
            propertyId = propertyId.toRequestBody(MultipartBody.FORM),
            description = description.toRequestBody(MultipartBody.FORM),
            files = files
        )
    }
}