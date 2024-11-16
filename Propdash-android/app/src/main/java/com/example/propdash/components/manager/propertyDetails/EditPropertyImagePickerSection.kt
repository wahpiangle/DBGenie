package com.example.propdash.components.manager.createProperty

import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPropertyImagePickerSection(
    selectedImages: List<ImageItem>,
    launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, List<Uri>>,
    onImageRemove: (Int) -> Unit
) {
    val carouselState = rememberCarouselState{ selectedImages.size }
    if (selectedImages.isEmpty()) {
        IconButton(
            onClick = { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            modifier = Modifier
                .width(420.dp)
                .height(250.dp)
                .border(1.dp, primary, RoundedCornerShape(8.dp))
        ) {
            Icon(Icons.Filled.AddCircleOutline, "Add Image", tint = light)
        }
    } else {
        HorizontalMultiBrowseCarousel(
            state = carouselState,
            modifier = Modifier.width(420.dp).height(250.dp),
            preferredItemWidth = 420.dp,
            itemSpacing = 8.dp
        ) { index ->
            val image = selectedImages[index]
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(image.asUriOrString())
                            .build(),
                        onError = { error ->
                            Log.e("ImagePickerSection", "Error loading image: $error")
                        }
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
                IconButton(
                    onClick = { onImageRemove(index) },
                    modifier = Modifier.size(32.dp).align(Alignment.TopEnd).padding(8.dp)
                ) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Remove Image", tint = Color.Red)
                }
            }
        }
    }
}
