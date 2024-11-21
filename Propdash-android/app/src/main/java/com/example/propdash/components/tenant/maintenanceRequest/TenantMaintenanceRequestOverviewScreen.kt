package com.example.propdash.components.tenant.maintenanceRequest

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.example.propdash.data.model.MaintenanceRequest
import com.example.propdash.ui.theme.grayText
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary
import com.example.propdash.viewModel.tenant.TenantMaintenanceDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantMaintenanceRequestOverviewScreen(
    maintenanceRequest: MaintenanceRequest,
    viewModel: TenantMaintenanceDetailViewModel
) {
    val carouselState = rememberCarouselState { maintenanceRequest.imageUrl.size }
    LazyColumn(
        verticalArrangement =
        Arrangement.Top,
        modifier = Modifier.fillMaxHeight(),
    ) {
        item {
            HorizontalMultiBrowseCarousel(
                state = carouselState,
                modifier = Modifier
                    .padding(bottom = 16.dp),
                preferredItemWidth = 250.dp,
                itemSpacing = 8.dp,
            ) { index ->
                val image = maintenanceRequest.imageUrl[index]
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
            Text("Status", color = light)
            Text(
                if (maintenanceRequest.resolved) "Resolved" else "Unresolved",
                color = grayText
            )
            Spacer(modifier = Modifier.padding(vertical = 16.dp))
            if (maintenanceRequest.description.isNotEmpty()) {
                Text("Description", color = light)
                Text(maintenanceRequest.description, color = grayText)
                Spacer(modifier = Modifier.padding(vertical = 16.dp))
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        viewModel.resolveMaintenanceRequest()
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primary
                    )
                ) {
                    Text("Mark as Resolved")
                }
            }
        }
    }
}