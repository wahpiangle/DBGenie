package com.example.propdash.components.tenant

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.propdash.components.manager.createProperty.ImageItem
import com.example.propdash.components.manager.createProperty.ImagePickerSection
import com.example.propdash.components.manager.createProperty.InputField
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary
import com.example.propdash.viewModel.tenant.TenantMaintenanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantCreateMaintenanceScreen(
    navigate: (String) -> Unit,
    viewModel: TenantMaintenanceViewModel
){
    var title by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<List<ImageItem>>(emptyList()) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) {
        selectedImageUri = it.map { uri -> ImageItem.FromUri(uri) }
    }
    val properties = viewModel.propertiesOfTenant.collectAsState()
    val carouselState = rememberCarouselState { selectedImageUri.size }
    var selectedPropertyId by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var imageError by remember { mutableStateOf(false) }
    var propertySelectionError by remember { mutableStateOf(false) }
    val context = LocalContext.current
    fun validate(): Boolean =
        if (title.isEmpty() || selectedImageUri.isEmpty()) {
            titleError = title.isEmpty()
            imageError = selectedImageUri.isEmpty()
            propertySelectionError = selectedPropertyId.isEmpty()
            false
        } else {
            true
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create Maintenance Request",
                        color = light,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigate(
                            TenantGraph.TenantMaintenanceScreen.route
                        )
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = light)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = dark)
            )
        },
        containerColor = dark
    ) {
        padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(12.dp)
        ){
            InputField(
                "Title",
                title,
                {
                    title = it
                    titleError = title.isEmpty()
                },
                titleError
            )
            InputField(
            "Description",
            description,
            {
                description = it;
            },
            false
        )
            Text(
                text = "Property",
                color = light,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Box (
                modifier = Modifier
                    .border(1.dp,
                        if (propertySelectionError) Color.Red else primary,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(16.dp)
                    .clickable { expanded = true }
                    .fillMaxWidth()
            ){
                Text(
                    text = if (selectedPropertyId.isEmpty()){
                        "Select Property"
                    } else {
                        properties.value.find { it.id == selectedPropertyId }?.name ?: "Select Property"
                    },
                    color = light,
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    properties.value.forEach { item ->
                        DropdownMenuItem(
                            onClick = {
                                selectedPropertyId = item.id
                                expanded = false
                            },
                            text = {
                                Text(text = item.name)
                            }
                        )
                    }
                }
            }

            Text("Images", color = light, modifier = Modifier.padding(vertical = 8.dp))
            ImagePickerSection(selectedImageUri, launcher, carouselState, onImageRemove = { index ->
                selectedImageUri = selectedImageUri.toMutableList().apply { removeAt(index) }
            })
            Button(
                modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = primary),
                onClick = {
                    if (validate()) {
                        viewModel.createMaintenanceRequest(description, title, selectedImageUri, selectedPropertyId, context)
                    }
                }
            ) {
                Text("Create Maintenance Request")
            }
        }

    }
}