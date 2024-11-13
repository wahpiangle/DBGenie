package com.example.propdash.components.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.propdash.components.manager.propertyDetails.DetailsScreen
import com.example.propdash.components.manager.propertyDetails.ManagerBookingsScreen
import com.example.propdash.components.manager.propertyDetails.RentalScreen
import com.example.propdash.components.manager.shared.BottomNavBar
import com.example.propdash.components.shared.PullToRefreshBox
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.errorBadge
import com.example.propdash.ui.theme.grayText
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary
import com.example.propdash.viewModel.manager.ManagerPropertyDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerPropertyDetailScreen(
    navigate: (String) -> Unit,
    viewModel: ManagerPropertyDetailViewModel
) {
    val property = viewModel.property.collectAsState()
    val error = viewModel.error.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Home", "About", "Settings")
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    if (property.value == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(dark),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading.value) {
                CircularProgressIndicator()
            } else {
                Text("Error: ${error.value}")
            }
        }
    } else {
        Scaffold(
            containerColor = dark,
            contentColor = dark,
            topBar = {
                TopAppBar(
                    title = {
                        Text(property.value!!.name)
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigate(ManagerScreen.ManagerPropertyScreen.route) }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
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
                                text = { Text("Edit") },
                                onClick = {
                                    expanded = false
                                    navigate(
                                        ManagerScreen.ManagerPropertyEditScreen.createRoute(property.value!!.id),
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete") },
                                onClick = {
                                    expanded = false
                                    showDeleteDialog = true
                                }
                            )
                        }

                        if (showDeleteDialog) {
                            AlertDialog(
                                onDismissRequest = { showDeleteDialog = false },
                                title = { Text("Delete Item") },
                                text = { Text("Are you sure you want to delete this item?") },
                                confirmButton = {
                                    TextButton(onClick = {
                                        viewModel.deleteProperty()
                                        showDeleteDialog = false
                                    }) {
                                        Text("Delete", color = errorBadge)
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showDeleteDialog = false }) {
                                        Text("Cancel")
                                    }
                                }
                            )
                        }
                    },
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
                        navigate(ManagerScreen.CreateBookingScreen.createRoute(property.value!!.id))
                    },
                    modifier = Modifier.size(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primary),
                ) {
                    Text(text = "+")
                }
            },
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Row {
                    tabs.forEachIndexed { index, title ->
                        Column(modifier = Modifier.weight(1f),
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
                    isRefreshing = isRefreshing,
                    onRefresh = viewModel::onPullToRefreshTrigger,
                    modifier = Modifier.fillMaxSize()
                ){
                    LazyColumn (
                        modifier = Modifier.fillMaxSize(),
                    ){
                        item {
                            when (tabIndex) {
                                0 -> ManagerBookingsScreen(
                                    property.value!!.booking ?: emptyList(),
                                )
                                1 -> DetailsScreen()
                                2 -> RentalScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}