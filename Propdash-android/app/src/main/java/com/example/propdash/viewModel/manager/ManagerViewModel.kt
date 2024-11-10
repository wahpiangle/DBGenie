package com.example.propdash.viewModel.manager

import android.util.Log
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

    fun createProperty(property: Property){

    }
}
