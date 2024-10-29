package com.example.propdash.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.propdash.data.model.Role
import com.example.propdash.data.model.User

@Composable
fun LoginScreen(onLoginSuccess: (String, String, String, Role, Boolean, String) -> Unit, userSession: User?) {
    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            Text("Login")
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Button(
                onClick = {
                    onLoginSuccess("1", "John Doe", "1", Role.TENANT, true, "cookie")
                }
            ) {
                Text(userSession.toString())
            }
        }
    }
}