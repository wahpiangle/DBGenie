package com.example.propdash.components.manager

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.example.propdash.components.manager.shared.BottomNavBar
import com.example.propdash.data.model.CreateProperty
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.grayText
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary
import com.example.propdash.viewModel.manager.ManagerViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerCreatePropertyScreen(navigate: (String) -> Unit, viewModel: ManagerViewModel) {
    var selectedImageUri by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val error by viewModel.error.collectAsState()
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) {
            selectedImageUri = it
        }
    val carouselState = rememberCarouselState { selectedImageUri.size }
    var nameError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var imageUrlError by remember { mutableStateOf(false) }
    val context = LocalContext.current
    fun validate(): Boolean {
        nameError = name.isEmpty()
        descriptionError = description.isEmpty()
        imageUrlError = selectedImageUri.isEmpty()
        return !nameError && !descriptionError && !imageUrlError
    }
    Scaffold(
        bottomBar = {
            BottomNavBar(
                ManagerScreen.ManagerPropertyScreen.route,
                navigate
            )
        },
        topBar = {
            Text(
                text = "Create Property", color = light, textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        },
        containerColor = dark
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Name", color = light)
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; nameError = false },
                label = { Text("Name") },
                textStyle = TextStyle(color = light),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primary,
                    unfocusedBorderColor = primary,
                    cursorColor = light,
                    unfocusedLabelColor = grayText,
                    focusedLabelColor = primary,
                ),
                trailingIcon = {
                    if (nameError)
                        Icon(Icons.Filled.Error, "error")
                },
                isError = nameError,
                supportingText = {
                    if (nameError)
                        Text("Name cannot be empty")
                }
            )
            Text(text = "Description", color = light)
            OutlinedTextField(
                value = description,
                onValueChange = { description = it; descriptionError = false },
                label = { Text("Description") },
                textStyle = TextStyle(color = light),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primary,
                    unfocusedBorderColor = primary,
                    cursorColor = light,
                    unfocusedLabelColor = grayText,
                    focusedLabelColor = primary,
                ),
                trailingIcon = {
                    if (descriptionError)
                        Icon(Icons.Filled.Error, "error")
                },
                isError = descriptionError,
                supportingText = {
                    if (descriptionError)
                        Text("Description cannot be empty")
                }
            )
            Text("Images", color = light, modifier = Modifier.padding(vertical = 16.dp))
            if (selectedImageUri.isEmpty()) {
                IconButton(
                    onClick = {
                        launcher.launch(
                            PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                        .width(420.dp)
                        .height(250.dp)
                        .border(1.dp, primary, RoundedCornerShape(8.dp))
                ) {
                    Icon(Icons.Filled.AddCircleOutline, "Add Image", tint = light)
                }
            }
            if (selectedImageUri.isNotEmpty()) {
                HorizontalMultiBrowseCarousel(
                    state = carouselState,
                    modifier = Modifier
                        .width(420.dp)
                        .height(250.dp),
                    preferredItemWidth = 300.dp,
                    itemSpacing = 8.dp,
                ) { i ->
                    val image = selectedImageUri[i]
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(data = image)
                                    .build()
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        IconButton(
                            onClick = {
                                selectedImageUri = selectedImageUri.toMutableList().apply {
                                    removeAt(i)
                                }
                            },
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.TopEnd)
                                .padding(8.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close, // Uses Material "X" close icon
                                contentDescription = "Remove Image",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
            if (imageUrlError) {
                Text("Please select an image", color = Color.Red)
            }
            Text(error ?: "", color = Color.Red)
            Button(
                onClick = {
                    if(validate()){
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