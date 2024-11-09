package com.example.propdash.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.propdash.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.storage.storage

class ManagerViewModel(private val userSession: User) : ViewModel() {
    var storageRef = Firebase.storage.reference.child("properties")
    fun test(){
        Log.d("ManagerViewModel", userSession.cookie)
    }
}
