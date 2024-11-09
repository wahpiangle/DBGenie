package com.example.propdash.components.tenant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.propdash.data.model.User

@Composable
fun TenantScreen(userSession: User?, clearSession: () -> Unit) {
    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            Text("Login")
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text(userSession.toString())
            Button(
                onClick = {
                    clearSession()
                }
            ) {
                Text("Logout")
            }
        }
    }
}