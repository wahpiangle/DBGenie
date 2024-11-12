package com.example.propdash.components.manager

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.errorBadge
import com.example.propdash.ui.theme.light
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

    LaunchedEffect(Unit) {
        viewModel.fetchPropertyData()
    }

    if (property.value == null) {
        Text("Loading...")
        return
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
            }
        ) { padding ->
            Text("dd", modifier = Modifier.padding(padding))
        }
    }
}