package com.example.propdash.viewModel.tenant

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propdash.components.manager.createProperty.ImageItem
import com.example.propdash.components.tenant.TenantGraph
import com.example.propdash.data.model.ErrorResponse
import com.example.propdash.data.model.MaintenanceRequest
import com.example.propdash.data.model.User
import com.example.propdash.data.repository.MaintenanceRequestRepository
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

class TenantMaintenanceDetailViewModel(
    private val maintenanceId: String,
    val userSession: User,
    private val navigate: (String) -> Unit
) : ViewModel() {
    private val maintenanceRepository = MaintenanceRequestRepository()
    private val _maintenanceRequest = MutableStateFlow<MaintenanceRequest?>(null)
    private val _maintenanceRequestError = MutableStateFlow<String?>(null)
    private val _isRefreshing = MutableStateFlow(false)
    val maintenanceRequestError = _maintenanceRequestError.asStateFlow()
    val maintenanceRequest = _maintenanceRequest.asStateFlow()
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        getMaintenanceRequest()
    }

    private fun getMaintenanceRequest() {
        viewModelScope.launch {
            try {
                val result = maintenanceRepository.getMaintenanceRequest(
                    userSession.cookie,
                    maintenanceId
                )
                if (result.isSuccessful) {
                    _maintenanceRequest.value = result.body()!!
                } else {
                    _maintenanceRequestError.value = Gson().fromJson(
                        result.errorBody()?.string(),
                        ErrorResponse::class.java
                    ).error
                }
            } catch (e: Exception) {
                _maintenanceRequestError.value = e.message
                e.printStackTrace()
            }
        }
    }

    fun resolveMaintenanceRequest() {
        viewModelScope.launch {
            try {
                val result = maintenanceRepository.resolveMaintenanceRequest(
                    userSession.cookie,
                    maintenanceId
                )
                if (result.isSuccessful) {
                    _maintenanceRequest.value?.resolved ?: true
                    onPullToRefreshTrigger()
                    navigate(TenantGraph.TenantMaintenanceScreen.route)
                } else {
                    _maintenanceRequestError.value = Gson().fromJson(
                        result.errorBody()?.string(),
                        ErrorResponse::class.java
                    ).error
                }
            } catch (e: Exception) {
                _maintenanceRequestError.value = e.message
                e.printStackTrace()
            }
        }
    }

    fun onPullToRefreshTrigger() {
        _isRefreshing.update { true }
        viewModelScope.launch {
            getMaintenanceRequest()
            _isRefreshing.update { false }
        }
    }

    fun createMaintenanceUpdate(
        description: String,
        selectedImageUri: ImageItem?,
        context: Context
    ) {
        viewModelScope.launch {
            try {
                val result = if (selectedImageUri == null) {
                    maintenanceRepository.createMaintenanceRequestUpdate(
                        userSession.cookie,
                        maintenanceId,
                        description,
                        MultipartBody.Part.createFormData("file", "")
                    )
                } else {
                    maintenanceRepository.createMaintenanceRequestUpdate(
                        userSession.cookie,
                        maintenanceId,
                        description,
                        prepareFileParts(selectedImageUri, context)
                    )
                }

                if (result.isSuccessful) {
                    onPullToRefreshTrigger()
                } else {
                    _maintenanceRequestError.value = Gson().fromJson(
                        result.errorBody()?.string(),
                        ErrorResponse::class.java
                    ).error
                }
            } catch (e: Exception) {
                _maintenanceRequestError.value = e.message
                e.printStackTrace()
            }
        }
    }

    private fun prepareFileParts(imageItem: ImageItem, context: Context): MultipartBody.Part {
        val filePath = imageItem.asUriOrString() as Uri
        val file = convertUriToFile(context, filePath)
        val filePart = MultipartBody.Part.createFormData(
            "file",
            file!!.name,
            file.asRequestBody("image/*".toMediaTypeOrNull())
        )
        return filePart
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
}