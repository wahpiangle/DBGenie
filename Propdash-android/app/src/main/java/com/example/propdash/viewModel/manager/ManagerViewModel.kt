package com.example.propdash.viewModel.manager

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propdash.data.model.Property
import com.example.propdash.data.model.User
import com.example.propdash.data.repository.PropertyRepository
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ManagerViewModel(private val userSession: User) : ViewModel() {
    var storageRef = Firebase.storage.reference.child("properties")
    private val _properties = MutableStateFlow<List<Property>>(emptyList())
    private val _error = MutableStateFlow<String?>(null)
    private val propertyRepository = PropertyRepository()
    val properties = _properties.asStateFlow()
    val error = _error.asStateFlow()

    fun fetchPropertyData(){
        viewModelScope.launch{
            try{
                val result = propertyRepository.getPropertiesByUser(userSession.cookie)
                _properties.value = result.body()!!
                Log.d("ManagerViewModel", "fetchPropertyData: ${_properties.value}")

            }catch(e: Exception){
                _error.value = e.message
                Log.e("ManagerViewModel", e.message!!)
            }
        }
    }

    fun createProperty(name:String, description:String, imageUrl:List<Uri>, context: Context){
        viewModelScope.launch{
            try{
                val result = propertyRepository.createProperty(
                    userSession.cookie,
                    name,
                    description,
                    userSession.id,
                    prepareFileParts(imageUrl, context)
                )

            }catch(e: Exception){
                _error.value = "There was an error creating the property. Please try again."
                Log.e("ManagerViewModel", e.message!!)
            }
        }
    }

    private fun prepareFileParts(filePaths: List<Uri>, context: Context): List<MultipartBody.Part> {
        val parts = mutableListOf<MultipartBody.Part>()
        for (filePath in filePaths) {
            val file = convertUriToFile(context, filePath)
            Log.d("ManagerViewModel", "prepareFileParts: $file");
            val filePart = MultipartBody.Part.createFormData(
                "files",
                file!!.name,
                RequestBody.create(MediaType.parse("image/*"), file)
            )
            parts.add(filePart)
        }
        return parts
    }
    private fun convertUriToFile(context: Context, uri: Uri): File?{
        val contentResolver = context.contentResolver
        val tempFile = File(context.cacheDir, "temp_${System.currentTimeMillis()}.jpg")  // You can change the file extension based on your needs
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
