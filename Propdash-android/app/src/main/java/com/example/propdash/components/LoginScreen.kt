package com.example.propdash.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.propdash.data.model.Role

@Composable
fun LoginScreen(onLoginSuccess: (String, String, String, Role, Boolean, String) -> Unit) {
    Scaffold(
        topBar = {
            Text("Login")
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Button(
                onClick = {}
            ) {
                Text("Login as Tenant")
            }
        }
    }
}