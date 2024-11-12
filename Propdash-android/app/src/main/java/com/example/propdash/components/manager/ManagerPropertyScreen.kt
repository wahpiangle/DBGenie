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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.propdash.components.manager.createProperty.PropertyCard
import com.example.propdash.components.manager.shared.BottomNavBar
import com.example.propdash.components.shared.PullToRefreshBox
import com.example.propdash.data.model.BookingStatus
import com.example.propdash.data.model.Property
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary
import com.example.propdash.viewModel.manager.ManagerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerPropertyScreen(
    navigate: (String) -> Unit,
    clearSession: () -> Unit,
    viewModel: ManagerViewModel
) {
    val properties = viewModel.properties.collectAsState()
    val error = viewModel.fetchPropertyError.collectAsState()
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.fetchPropertyData()
    }
    Scaffold(
        topBar = {
            Text(
                "Manage Properties",
                color = light, textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                fontSize = 24.sp,
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
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = viewModel::onPullToRefreshTrigger,
            modifier = Modifier.padding(padding)
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    items(state.items){ item: Property ->
                        PropertyCard(
                            propertyName = item.name,
                            price = item.rentalPerMonth,
                            status = BookingStatus.VACANT,
                            imageUrl = item.imageUrl[0],
                            navigate = navigate
                        )
                    }
                }
            }
        }
    }
}