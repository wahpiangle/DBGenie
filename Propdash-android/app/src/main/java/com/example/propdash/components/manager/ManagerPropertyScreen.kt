package com.example.propdash.components.manager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.propdash.components.manager.shared.BottomNavBar
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.light
import com.example.propdash.viewModel.manager.ManagerViewModel

@Composable
fun ManagerPropertyScreen(navigate: (String) -> Unit, viewModel: ManagerViewModel) {
    val storageRef = viewModel.storageRef
    val properties = viewModel.properties.collectAsState(emptyList())
    val error = viewModel.error.collectAsState(null)
    LaunchedEffect(Unit){
        viewModel.fetchPropertyData()
    }
    Scaffold(
        bottomBar = {
            BottomNavBar(
                ManagerScreen.ManagerPropertyScreen.route,
                navigate)
        },
        floatingActionButton = {
            Button(
                onClick = {
                    navigate("manager_property_create_screen")
                },
                modifier = Modifier.size(56.dp)
            ) {
                Text(text = "+")
            }
        },
        containerColor = dark
    ) { padding ->
        Column (
            modifier = Modifier.padding(padding).fillMaxWidth().fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ){
            if(error.value != null){
                Text(text = error.value!!)
            }
            if(properties.value.isEmpty()) {
                Text(text = "No properties found",
                    color = light
                )
            }else{
                properties.value.forEach {
                    Text(text = it.name)
                }
            }
        }
    }
}