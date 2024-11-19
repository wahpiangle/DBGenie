package com.example.propdash.components.manager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.propdash.components.manager.shared.BottomNavBar
import com.example.propdash.ui.theme.dark

@Composable
fun ManagerMaintenanceScreen(
    navigate: (String) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                ManagerScreen.ManagerMaintenanceScreen.route,
                navigate)
        },
        containerColor = dark
    ) { padding ->
        Column (
            modifier = Modifier.padding(padding)
        ){
        }
    }
}