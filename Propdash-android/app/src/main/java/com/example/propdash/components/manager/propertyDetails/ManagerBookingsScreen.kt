package com.example.propdash.components.manager.propertyDetails

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.propdash.data.model.Booking
import com.example.propdash.ui.theme.light

@Composable
fun ManagerBookingsScreen(
    booking: List<Booking>
) {
    if(booking.isEmpty()){
        Text("No Bookings", color = light)
    } else {
        Text("Bookings")
    }
}