package com.example.propdash.components.manager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.propdash.components.manager.shared.BottomNavBar
import com.example.propdash.ui.theme.dark
import com.example.propdash.viewModel.ManagerViewModel

@Composable
fun ManagerPropertyScreen(navigate: (String) -> Unit, viewModel: ManagerViewModel) {
    val storageRef = viewModel.storageRef
    Scaffold(
        bottomBar = {
            BottomNavBar(
                ManagerScreen.ManagerPropertyScreen.route,
                navigate)
        },
        containerColor = dark
    ) { padding ->
        Column (
            modifier = Modifier.padding(padding)
        ){
            Button(
                onClick = {
                    viewModel.test()
                },
                modifier = Modifier.size(200.dp)
            ) {
                Text("Test")
            }
        }
    }
}