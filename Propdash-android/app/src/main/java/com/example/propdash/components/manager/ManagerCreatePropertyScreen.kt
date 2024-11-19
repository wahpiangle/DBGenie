package com.example.propdash.components.manager

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import com.example.propdash.components.manager.shared.BottomNavBar
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.light
import com.example.propdash.viewModel.manager.ManagerCreatePropertyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerCreatePropertyScreen(navigate: (String) -> Unit, viewModel: ManagerCreatePropertyViewModel) {
    var selectedImageUri by remember { mutableStateOf<List<ImageItem>>(emptyList()) }
    val error by viewModel.createPropertyError.collectAsState()
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) {
            selectedImageUri = it.map { uri -> ImageItem.FromUri(uri) }
        }
    val carouselState = rememberCarouselState { selectedImageUri.size }

    fun validate(): Boolean =
        if (name.isEmpty() || description.isEmpty() || selectedImageUri.isEmpty()) {
            nameError = name.isEmpty()
            descriptionError = description.isEmpty()
            false
        } else {
            nameError = false
            descriptionError = false
            true
        }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                ManagerScreen.ManagerPropertyScreen.route,
                navigate
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create Property",
                        color = light,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigate(
                            ManagerScreen.ManagerPropertyScreen.route
                        )
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = light)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = dark)
            )
        },
        containerColor = dark
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            InputField(
                "Name",
                name,
                {
                    name = it
                    nameError = name.isEmpty()
                },
                nameError
            )
            InputField(
                "Description",
                description, {
                    description = it
                    descriptionError = description.isEmpty()
                },
                descriptionError
            )
            Text("Images", color = light, modifier = Modifier.padding(vertical = 16.dp))
            ImagePickerSection(selectedImageUri, launcher, carouselState, onImageRemove = { index ->
                selectedImageUri = selectedImageUri.toMutableList().apply { removeAt(index) }
            })
            Text(error ?: "", color = Color.Red)
            Button(
                onClick = {
                    if (validate()) {
                        viewModel.createProperty(
                            name,
                            description,
                            selectedImageUri,
                            context
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Create Property")
            }
        }

    }
}