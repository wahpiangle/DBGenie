package com.example.propdash.components.tenant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.propdash.components.shared.PullToRefreshBox
import com.example.propdash.components.tenant.shared.TenantBottomNavBar
import com.example.propdash.components.tenant.shared.TenantPropertyCard
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary
import com.example.propdash.ui.theme.successBadge
import com.example.propdash.viewModel.tenant.TenantBookingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantBookingsScreen(
    viewModel: TenantBookingViewModel,
    clearSession: () -> Unit,
    navigate: (String) -> Unit
){
    val bookings = viewModel.bookings.collectAsState()
    val bookingError = viewModel.bookingError.collectAsState()
    val isRefreshing = viewModel.isRefreshing.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Manage Bookings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    Button(
                        onClick = {
                            clearSession()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primary,
                        )
                    ) {
                        Text(text = "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = dark,
                    titleContentColor = light,
                    navigationIconContentColor = light,
                ),
            )
        },
        bottomBar = {
            TenantBottomNavBar (
                currentRoute = TenantGraph.TenantBookingsScreen.route,
                navigate = navigate
            )
        },
        containerColor = dark
    ) {padding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = viewModel::onPullToRefreshTrigger,
            modifier = Modifier.padding(padding)
        ) {
            if (bookingError.value != null) {
                Text(text = bookingError.value!!)
            }

            if (bookings.value.isEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    item {
                        Text(
                            text = "No bookings found",
                            color = light
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    items(bookings.value) { booking ->
                        TenantPropertyCard (
                            title = booking.property.name,
                            imageUrl = booking.property.imageUrl[0],
                            detailsText = "$ ${booking.rentalPrice} per month",
                            Badge = {
                                Text(
                                    text = "Status",
                                    style = TextStyle(fontSize = 16.sp),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .background(
                                            color = successBadge,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .padding(horizontal = 16.dp, vertical = 6.dp)
                                )
                            },
                            navigate = {
                                navigate(
                                    TenantGraph.TenantBookingDetailScreen.createRoute(booking.id)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}