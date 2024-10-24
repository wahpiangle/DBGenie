package com.example.propdash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.propdash.components.MainNavGraph
import com.example.propdash.ui.theme.PropdashTheme
import com.example.propdash.viewModel.UserViewModel

class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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