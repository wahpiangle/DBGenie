package com.example.propdash.viewModel.manager

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propdash.components.manager.ManagerScreen
import com.example.propdash.components.manager.createProperty.ImageItem
import com.example.propdash.data.model.CreateProperty
import com.example.propdash.data.model.ErrorResponse
import com.example.propdash.data.model.Property
import com.example.propdash.data.model.User
import com.example.propdash.data.repository.PropertyRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class ManagerPropertyDetailViewModel(
    val userSession: User,
    private val propertyId: String,
    private val navigate: (String) -> Unit
): ViewModel() {
    private val propertyRepository = PropertyRepository()
    private val _property = MutableStateFlow<Property?>(null)
    private val _error = MutableStateFlow<String?>(null)
    private val _isRefreshing = MutableStateFlow(false)
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    val isRefreshing = _isRefreshing.asStateFlow()
    val property = _property.asStateFlow()
    val error = _error.asStateFlow()

    init{
        fetchPropertyData()
    }

    private fun fetchPropertyData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = propertyRepository.getProperty(userSession.cookie, propertyId)
                if(result.isSuccessful) {
                    if(result.body() == null){
                        _error.value = "Property not found"
                        return@launch
                    }
                    _property.value = result.body()!!
                }else{
                    val errorResponse = Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java)
                    _error.value = errorResponse.error
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.message
                Log.e("ManagerViewModel", e.message!!)
            }
        }
    }

    fun deleteProperty() {
        viewModelScope.launch {
            try {
                propertyRepository.removeProperty(userSession.cookie, propertyId)
                _property.value = null
                navigate(ManagerScreen.ManagerPropertyScreen.route)
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("ManagerViewModel", e.message!!)
            }
        }
    }

    fun updateProperty(
        name: String,
        description: String,
        imageItemList: List<ImageItem>,
        updateImage: Boolean,
        context: Context
    ){
        viewModelScope.launch {
            try {
                propertyRepository.updateProperty(propertyId, userSession.cookie, CreateProperty(
                    name = name,
                    description = description,
                    userId = userSession.id,
                    imageUrl = prepareFileParts(imageItemList, context),
                    updateImage
                ))
                navigate(ManagerScreen.ManagerPropertyScreen.route)
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("ManagerViewModel", e.message!!)
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
            fetchPropertyData()
            _isRefreshing.update { false }
        }
    }
}