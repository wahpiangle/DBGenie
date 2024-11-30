package com.example.propdash.components.manager.propertyDetails

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.example.propdash.ui.theme.grayText
import com.example.propdash.ui.theme.light
import com.example.propdash.viewModel.manager.ManagerPropertyDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    viewModel: ManagerPropertyDetailViewModel,
) {
    val property = viewModel.property.collectAsState()
    val carouselState = rememberCarouselState { property.value?.imageUrl?.size ?: 0 }
    Column (
        modifier = Modifier
            .padding(16.dp)
    ){
        HorizontalMultiBrowseCarousel(
            state = carouselState,
            modifier = Modifier
                .padding(bottom = 16.dp),
            preferredItemWidth = 300.dp,
            itemSpacing = 8.dp,
        ) { index ->
            val image = property.value?.imageUrl?.get(index)
            Box {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current).data(image).build(),
                        onError = { error ->
                            Log.e("DetailsScreen", "Error loading image: $error")
                        }
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.clip(
                        shape = RoundedCornerShape(8.dp)
                    ),
                )
            }
        }
        Text(
            "Description",
            color = light,
        )
        Text(
            property.value?.description ?: "",
            color = grayText,
        )
    }
}