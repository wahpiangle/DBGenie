package com.example.propdash.viewModel.manager

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propdash.components.manager.ManagerScreen
import com.example.propdash.components.manager.createProperty.ImageItem
import com.example.propdash.data.model.User
import com.example.propdash.data.repository.PropertyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class ManagerCreatePropertyViewModel(
    private val userSession: User,
    private val navigate: (String) -> Unit
) : ViewModel() {
    private val _createPropertyError = MutableStateFlow<String?>(null)
    private val propertyRepository = PropertyRepository()
    val createPropertyError = _createPropertyError.asStateFlow()

    fun createProperty(
        name: String,
        description: String,
        rentalPerMonth: String,
        imageUrl: List<ImageItem>,
        context: Context
    ) {
        viewModelScope.launch {
            try {
                val result = propertyRepository.createProperty(
                    userSession.cookie,
                    name,
                    description,
                    userSession.id,
                    prepareFileParts(imageUrl, context)
                )
                if (result.isSuccessful) {
                    navigate(ManagerScreen.ManagerPropertyScreen.route)
                }
            } catch (e: Exception) {
                _createPropertyError.value =
                    "There was an error creating the property. Please try again."
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
}
