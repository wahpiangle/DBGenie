package com.example.propdash.components.manager

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.propdash.components.manager.createProperty.PropertyCard
import com.example.propdash.components.manager.shared.BottomNavBar
import com.example.propdash.data.model.BookingStatus
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary
import com.example.propdash.viewModel.manager.ManagerViewModel

@Composable
fun ManagerPropertyScreen(
    navigate: (String) -> Unit,
    clearSession: () -> Unit,
    viewModel: ManagerViewModel
) {
    val storageRef = viewModel.storageRef
    val properties = viewModel.properties.collectAsState()
    val error = viewModel.fetchPropertyError.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchPropertyData()
    }
    Scaffold(
        topBar={
            Text("Manage Properties",
                color = light, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                fontSize=24.sp,
                fontWeight = FontWeight.Bold
            )
        },
        bottomBar = {
            BottomNavBar(
                ManagerScreen.ManagerPropertyScreen.route,
                navigate
            )
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
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (error.value != null) {
                Text(text = error.value!!)
            }
            if (properties.value.isEmpty()) {
                Text(
                    text = "No properties found",
                    color = light
                )

            } else {
                properties.value.forEach {
                    PropertyCard(
                        propertyName = it.name,
                        price = it.rentalPerMonth,
                        status = BookingStatus.VACANT,
                        imageUrl = it.imageUrl[0],
                        navigate = navigate
                    )
                }
            }
        }
    }
}