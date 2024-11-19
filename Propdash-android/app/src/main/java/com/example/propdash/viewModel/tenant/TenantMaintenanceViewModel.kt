package com.example.propdash.viewModel.tenant

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propdash.components.manager.createProperty.ImageItem
import com.example.propdash.components.tenant.TenantGraph
import com.example.propdash.data.model.ErrorResponse
import com.example.propdash.data.model.MaintenanceRequest
import com.example.propdash.data.model.Property
import com.example.propdash.data.model.User
import com.example.propdash.data.repository.MaintenanceRequestRepository
import com.example.propdash.data.repository.PropertyRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class TenantMaintenanceViewModel(
    private val userSession: User,
    private val navigate: (String) -> Unit
) : ViewModel() {

    private val maintenanceRequestRepository = MaintenanceRequestRepository()
    private val propertyRepository = PropertyRepository()
    private val _maintenanceRequests = MutableStateFlow<List<MaintenanceRequest>>(emptyList())
    private val _maintenanceError = MutableStateFlow<String?>(null)
    private val _isRefreshing = MutableStateFlow(false)
    private val _isLoading = MutableStateFlow(false)
    private val _propertiesOfTenant = MutableStateFlow<List<Property>>(emptyList())
    val maintenanceRequests = _maintenanceRequests.asStateFlow()
    val propertiesOfTenant = _propertiesOfTenant.asStateFlow()
    val maintenanceError = _maintenanceError.asStateFlow()
    val isRefreshing = _isRefreshing.asStateFlow()
    val isLoading = _isLoading.asStateFlow()

    init {
        getMaintenanceRequests()
        getPropertiesOfTenant()
    }

    private fun getMaintenanceRequests() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = maintenanceRequestRepository.getMaintenanceRequests(
                    userSession.cookie
                )
                if (result.isSuccessful) {
                    _maintenanceRequests.value = result.body()!!
                } else {
                    _maintenanceError.value = result.errorBody()?.string()
                }
            } catch (e: Exception) {
                _maintenanceError.value = e.message
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }
        }
    }

    private fun getPropertiesOfTenant() {
        viewModelScope.launch {
            try {
                val result = propertyRepository.getPropertiesByUser(
                    userSession.cookie
                )
                if (result.isSuccessful) {
                    _propertiesOfTenant.value = result.body()!!
                } else {
                    val errorResponse = Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java)
                    _maintenanceError.value = errorResponse.error
                }
            } catch (e: Exception) {
                _maintenanceError.value = e.message
                e.printStackTrace()
            }
        }
    }

    fun createMaintenanceRequest(description: String, images: List<ImageItem>, propertyId:String, context: Context) {
        viewModelScope.launch {
            try {
                val result = maintenanceRequestRepository.createMaintenanceRequest(
                    userSession.cookie,
                    propertyId = propertyId,
                    description = description,
                    prepareFileParts(images, context)
                )
                if (result.isSuccessful) {
                    getMaintenanceRequests()
                    navigate(TenantGraph.TenantMaintenanceScreen.route)
                } else {
                    val errorResponse = Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java)
                    _maintenanceError.value = errorResponse.error
                }
            } catch (e: Exception) {
                _maintenanceError.value = e.message
                e.printStackTrace()
            }
        }
    }

    private fun prepareFileParts(imageItemList: List<ImageItem>, context: Context): List<MultipartBody.Part> {
        val filePaths = imageItemList.mapNotNull {
            when (it) {
                is ImageItem.FromUri -> it.uri
                else -> null
            }
        }
        val parts = mutableListOf<MultipartBody.Part>()
        for (filePath in filePaths) {
            val file = convertUriToFile(context, filePath)
            val filePart = MultipartBody.Part.createFormData(
                "files",
                file!!.name,
                file.asRequestBody("image/*".toMediaTypeOrNull())
            )
            parts.add(filePart)
        }
        return parts
    }

    private fun convertUriToFile(context: Context, uri: Uri): File? {
        val contentResolver = context.contentResolver
        val tempFile = File(
            context.cacheDir,
            "temp_${System.currentTimeMillis()}.jpg"
        )
        tempFile.createNewFile()

        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val outputStream: OutputStream = FileOutputStream(tempFile)

            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return tempFile
    }

    fun onPullToRefreshTrigger() {
        _isRefreshing.update { true }
        viewModelScope.launch {
            _maintenanceError.value = null
            getMaintenanceRequests()
            _isRefreshing.update { false }
        }
    }
}