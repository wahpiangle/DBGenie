package com.example.propdash.components.manager.maintenanceDetail

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.example.propdash.components.manager.createProperty.ImageItem
import com.example.propdash.data.model.MaintenanceRequest
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary
import com.example.propdash.viewModel.manager.ManagerMaintenanceRequestDetailViewModel

@Composable
fun ManagerMaintenanceRequestUpdatesScreen(
    maintenanceRequest: MaintenanceRequest,
    viewModel: ManagerMaintenanceRequestDetailViewModel
) {
    var input by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<ImageItem?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        selectedImageUri = ImageItem.FromUri(it!!)
    }
    val context = LocalContext.current
    val userSession = viewModel.userSession
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight()
    ) {
        if (maintenanceRequest.maintenanceRequestUpdates.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                items(maintenanceRequest.maintenanceRequestUpdates) { maintenanceRequestUpdate ->
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = maintenanceRequestUpdate.userId.let {
                            if (it == userSession.id) {
                                Arrangement.End
                            } else {
                                Arrangement.Start
                            }
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (maintenanceRequestUpdate.userId == userSession.id) {
                                        primary
                                    } else {
                                        light
                                    }
                                )
                        ) {
                            if (maintenanceRequestUpdate.imageUrl.isNotEmpty()) {
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        ImageRequest.Builder(LocalContext.current)
                                            .data(maintenanceRequestUpdate.imageUrl)
                                            .build(),
                                    ),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .height(250.dp)
                                        .padding(8.dp)
                                        .background(
                                            if (maintenanceRequestUpdate.userId == userSession.id) {
                                                primary
                                            } else {
                                                light
                                            }
                                        )
                                )
                            } else {
                                Text(
                                    text = maintenanceRequestUpdate.description,
                                    color = if (maintenanceRequestUpdate.userId == userSession.id) {
                                        light
                                    } else {
                                        Color.Black
                                    },
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .background(light)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = {
                    launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
            ) {
                Icon(
                    Icons.Outlined.AddCircleOutline,
                    contentDescription = "Add"
                )
            }
            if (selectedImageUri != null) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(selectedImageUri!!.asUriOrString())
                                .build(),
                        ),
                        contentDescription = "Image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.height(120.dp),
                    )
                    IconButton(
                        onClick = { selectedImageUri = null },
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Remove Image",
                            tint = Color.Red
                        )
                    }
                }
            } else {
                TextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp)),
                    placeholder = { Text("Add a new update...") },
                )
            }

            IconButton(
                onClick = {
                    if (selectedImageUri == null && input.isEmpty()) {
                        return@IconButton
                    }
                    viewModel.createMaintenanceUpdate(
                        input,
                        selectedImageUri,
                        context
                    )
                    input = ""
                    selectedImageUri = null
                },
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.Send,
                    contentDescription = "Send"
                )
            }
        }
    }
}