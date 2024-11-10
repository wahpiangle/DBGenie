package com.example.propdash.components.manager

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.propdash.viewModel.manager.ManagerViewModel
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.example.propdash.components.manager.shared.BottomNavBar
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.light

@Composable
fun ManagerCreatePropertyScreen(navigate: (String) -> Unit, viewModel: ManagerViewModel) {
    var selectedImageUri by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) {
        selectedImageUri = it
    }
    Scaffold(
        bottomBar = {
            BottomNavBar(
                ManagerScreen.ManagerPropertyScreen.route,
                navigate)
        },

        containerColor = dark
    ) { padding ->
        Column (
            modifier = Modifier.padding(padding).fillMaxWidth().fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ){
            Button(onClick = {
                launcher.launch(
                    PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }) {
                Text(text = "Select Image")
            }
            selectedImageUri.forEach { image ->
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest
                            .Builder(LocalContext.current)
                            .data(data = image)
                            .build()
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(150.dp, 150.dp)
                        .padding(16.dp)
                )
            }
        }

    }
}