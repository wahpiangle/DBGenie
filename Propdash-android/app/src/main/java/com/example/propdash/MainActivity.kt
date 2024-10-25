package com.example.propdash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.propdash.components.MainNavGraph
import com.example.propdash.data.SessionManager
import com.example.propdash.ui.theme.PropdashTheme
import com.example.propdash.viewModel.UserViewModel
import com.example.propdash.viewModel.UserViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sessionManager = SessionManager(applicationContext)
        val factory = UserViewModelFactory(sessionManager)
        val userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
        enableEdgeToEdge()
        setContent {
            PropdashTheme {
                MainNavGraph(
                    userViewModel = userViewModel
                )
            }
        }
    }
}