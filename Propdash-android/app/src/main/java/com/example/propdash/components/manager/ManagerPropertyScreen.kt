package com.example.propdash.components.manager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.propdash.components.manager.createProperty.PropertyCard
import com.example.propdash.components.manager.shared.BottomNavBar
import com.example.propdash.components.shared.PullToRefreshBox
import com.example.propdash.data.model.Booking
import com.example.propdash.data.model.BookingStatus
import com.example.propdash.data.model.Property
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary
import com.example.propdash.viewModel.manager.ManagerPropertyViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerPropertyScreen(
    navigate: (String) -> Unit,
    clearSession: () -> Unit,
    viewModel: ManagerPropertyViewModel
) {
    val error = viewModel.fetchPropertyError.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val properties by viewModel.properties.collectAsState()
    fun getBookingStatus(bookings: List<Booking>?): BookingStatus {
        if (bookings.isNullOrEmpty()) {
            return BookingStatus.VACANT
        }
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        for (booking in bookings) {
            val checkIn = simpleDateFormat.parse(booking.checkIn)?.time
            val checkOut = simpleDateFormat.parse(booking.checkOut)?.time
            val currentTime = System.currentTimeMillis()
            if (currentTime in checkIn!!..checkOut!!) {
                return BookingStatus.OCCUPIED
            }
        }
        return BookingStatus.VACANT
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Manage Properties",
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
            BottomNavBar(
                ManagerScreen.ManagerPropertyScreen.route,
                navigate
            )
        },
        floatingActionButton = {
            Button(
                onClick = {
                    navigate(ManagerScreen.ManagerCreatePropertyScreen.route)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary,
                ),
                modifier = Modifier.size(56.dp)
            ) {
                Text(text = "+")
            }
        },
        containerColor = dark
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = viewModel::onPullToRefreshTrigger,
            modifier = Modifier.padding(padding)
        ) {
            if (error.value != null) {
                Text(text = error.value!!, color = light)
            }

            if (properties.isEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    item {
                        Text(
                            text = "No properties found",
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
                    items(properties){ item: Property ->
                        PropertyCard(
                            propertyId = item.id,
                            propertyName = item.name,
                            status = getBookingStatus(item.bookings),
                            imageUrl = item.imageUrl[0],
                            navigate = navigate
                        )
                    }
                }
            }
        }
    }
}