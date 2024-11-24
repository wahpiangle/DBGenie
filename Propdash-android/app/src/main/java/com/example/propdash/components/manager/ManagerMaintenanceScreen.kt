package com.example.propdash.components.manager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.propdash.components.manager.shared.BottomNavBar
import com.example.propdash.components.shared.MaintenanceCard
import com.example.propdash.components.shared.PullToRefreshBox
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.light
import com.example.propdash.viewModel.manager.ManagerMaintenanceRequestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerMaintenanceScreen(
    navigate: (String) -> Unit,
    viewModel: ManagerMaintenanceRequestViewModel
) {
    val maintenanceRequests = viewModel.maintenanceRequests.collectAsState()
    val maintenanceError = viewModel.maintenanceError.collectAsState()
    val isRefreshing = viewModel.isRefreshing.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                ManagerScreen.ManagerMaintenanceScreen.route,
                navigate)
        },
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
                actions = {
                    IconButton(
                        onClick = { expanded = true },
                        colors = IconButtonDefaults.iconButtonColors(contentColor = light)
                    ) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("All Requests") },
                            onClick = {
                                viewModel.applyResolvedFilter(null)
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Resolved") },
                            onClick = {
                                viewModel.applyResolvedFilter(true)
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Unresolved") },
                            onClick = {
                                viewModel.applyResolvedFilter(false)
                                expanded = false
                            }
                        )
                    }
                }
            )
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
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    items(maintenanceRequests.value) { maintenanceRequest ->
                        MaintenanceCard(
                            title = maintenanceRequest.title,
                            propertyName = maintenanceRequest.property.name,
                            status = if(maintenanceRequest.resolved) "Resolved" else "Unresolved",
                            imageUrl = maintenanceRequest.imageUrl[0],
                            navigate = { navigate(
                                ManagerScreen.ManagerMaintenanceDetailScreen.createRoute(
                                    maintenanceRequest.id
                            )) }
                        )
                    }
                }
            }
        }
    }
}