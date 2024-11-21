package com.example.propdash.components.tenant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.propdash.components.shared.PullToRefreshBox
import com.example.propdash.components.tenant.maintenanceRequest.MaintenanceRequestOverviewScreen
import com.example.propdash.components.tenant.maintenanceRequest.MaintenanceRequestUpdatesScreen
import com.example.propdash.components.tenant.shared.TenantBottomNavBar
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.grayText
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary
import com.example.propdash.viewModel.tenant.TenantMaintenanceDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantMaintenanceDetailScreen(
    viewModel: TenantMaintenanceDetailViewModel,
    navigate: (String) -> Unit
) {
    val tabs = listOf("Overview", "Updates")
    var tabIndex by remember { mutableIntStateOf(0) }
    val maintenanceRequest = viewModel.maintenanceRequest.collectAsState()
    val maintenanceRequestError = viewModel.maintenanceRequestError.collectAsState()
    val isRefreshing = viewModel.isRefreshing.collectAsState()

    if (maintenanceRequestError.value != null) {
        Text(text = maintenanceRequestError.value!!)
        return
    }

    if (maintenanceRequest.value == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(dark),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator()
        }
        return
    } else {
        Scaffold(
            containerColor = dark,
            contentColor = dark,
            topBar = {
                TopAppBar(
                    title = {
                        Text(maintenanceRequest.value!!.title)
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigate(TenantGraph.TenantMaintenanceScreen.route) }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
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
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                Row {
                    tabs.forEachIndexed { index, title ->
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Tab(
                                text = {
                                    Text(
                                        title,
                                        color = if (tabIndex == index) primary else grayText
                                    )
                                },
                                selected = tabIndex == index,
                                onClick = { tabIndex = index }
                            )
                            HorizontalDivider(
                                color = if (tabIndex == index) primary else Color.Transparent,
                                thickness = 3.dp,
                                modifier = Modifier.width(100.dp)
                            )
                        }
                    }
                }
                HorizontalDivider(
                    color = primary
                )
                PullToRefreshBox(
                    isRefreshing = isRefreshing.value,
                    onRefresh = viewModel::onPullToRefreshTrigger,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    when (tabIndex) {
                        0 -> MaintenanceRequestOverviewScreen(
                                maintenanceRequest = maintenanceRequest.value!!,
                                viewModel = viewModel
                            )
                        1 -> MaintenanceRequestUpdatesScreen(
                            maintenanceRequest = maintenanceRequest.value!!,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}
