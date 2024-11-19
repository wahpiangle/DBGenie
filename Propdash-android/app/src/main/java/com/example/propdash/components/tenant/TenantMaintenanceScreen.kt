package com.example.propdash.components.tenant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.propdash.components.shared.MaintenanceCard
import com.example.propdash.components.shared.PullToRefreshBox
import com.example.propdash.components.tenant.shared.TenantBottomNavBar
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary
import com.example.propdash.viewModel.tenant.TenantMaintenanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantMaintenanceScreen(
    navigate: (String) -> Unit,
    viewModel: TenantMaintenanceViewModel
) {
    val maintenanceRequests = viewModel.maintenanceRequests.collectAsState()
    val maintenanceError = viewModel.maintenanceError.collectAsState()
    val isRefreshing = viewModel.isRefreshing.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Maintenance Requests",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = dark,
                    titleContentColor = light,
                    navigationIconContentColor = light,
                ),
            )
        },
        bottomBar = {
            TenantBottomNavBar(
                currentRoute = TenantGraph.TenantMaintenanceScreen.route,
                navigate = navigate
            )
        },
        floatingActionButton = {
            Button(
                onClick = {
                    navigate(TenantGraph.TenantCreateMaintenanceScreen.route)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary,
                ),
                modifier = Modifier.size(56.dp)
            ) {
                Text(text = "+")
            }
        },
        containerColor = dark
    ) { padding ->
        if(isLoading.value){
            CircularProgressIndicator()
            return@Scaffold
        }
        PullToRefreshBox(
            isRefreshing = isRefreshing.value,
            onRefresh = viewModel::onPullToRefreshTrigger,
            modifier = Modifier.padding(padding)
        ) {
            if (maintenanceError.value != null) {
                Text(text = maintenanceError.value!!, color = light)
                return@PullToRefreshBox
            }

            if (maintenanceRequests.value.isEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    item {
                        Text(
                            text = "No maintenance requests",
                            color = light
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    items(maintenanceRequests.value) { maintenanceRequest ->
                        MaintenanceCard(
                            title = maintenanceRequest.description,
                            propertyName = maintenanceRequest.property.name,
                            status = if(maintenanceRequest.resolved) "Resolved" else "Unresolved",
                            imageUrl = maintenanceRequest.imageUrl[0],
                            navigate = { navigate(TenantGraph.TenantMaintenanceDetailScreen.route) }
                        )
                    }
                }
            }
        }
    }
}