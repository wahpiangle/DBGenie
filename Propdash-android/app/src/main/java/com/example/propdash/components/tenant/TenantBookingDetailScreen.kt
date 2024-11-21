package com.example.propdash.components.tenant

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.grayText
import com.example.propdash.ui.theme.light
import com.example.propdash.viewModel.tenant.TenantBookingDetailViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantBookingDetailScreen(
    navigate: (String) -> Unit,
    viewModel: TenantBookingDetailViewModel
) {
    val booking = viewModel.booking.collectAsState()
    val bookingError = viewModel.bookingError.collectAsState().value
    val carouselState = rememberCarouselState { booking.value?.property?.imageUrl?.size ?: 0 }

    fun formatIsoDateToDDMMYYYY(isoDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date: Date? = inputFormat.parse(isoDate)
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return date?.let { outputFormat.format(it) } ?: "Invalid Date"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Booking Details",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigate(TenantGraph.TenantBookingsScreen.route) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = dark,
                    titleContentColor = light,
                    navigationIconContentColor = light,
                ),
            )
        },
        containerColor = dark
    ) { padding ->
        if (booking.value == null) {
            if (bookingError != null) {
                Text(
                    text = bookingError,
                    modifier = Modifier.padding(padding)
                )
                return@Scaffold
            }
            CircularProgressIndicator()
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(8.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            HorizontalMultiBrowseCarousel(
                state = carouselState,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .height(400.dp),
                preferredItemWidth = 300.dp,
                itemSpacing = 8.dp,
            ) { index ->
                val image = booking.value?.property?.imageUrl?.get(index)
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
                booking.value?.property?.description ?: "",
                color = grayText,
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text("Booking Remarks",
                color = light,
                )
            Text(
                booking.value?.remarks ?: "",
                color = grayText,
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text("Rental Period",
                color = light,
                )
            Text(
                "${formatIsoDateToDDMMYYYY(booking.value!!.checkIn)} - ${formatIsoDateToDDMMYYYY(booking.value!!.checkOut)}",
                color = grayText,
            )
        }
    }
}