package com.example.propdash.components.manager.propertyDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.propdash.components.manager.ManagerScreen
import com.example.propdash.components.shared.EditDeleteMenu
import com.example.propdash.data.model.Booking
import com.example.propdash.ui.theme.errorBadge
import com.example.propdash.ui.theme.grayText
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary
import com.example.propdash.ui.theme.successBadge
import com.example.propdash.viewModel.manager.ManagerBookingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ManagerBookingsScreen(
    booking: List<Booking>,
    viewModel: ManagerBookingViewModel,
    propertyId: String
) {
    val deleteBookingError by viewModel.deleteBookingError.collectAsState()

    fun formatIsoDateToDDMMYYYY(isoDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date: Date? = inputFormat.parse(isoDate)
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return date?.let { outputFormat.format(it) } ?: "Invalid Date"
    }

    fun getBookingStatus(checkInDate: String, checkOutDate: String): String {
        val currentTime = System.currentTimeMillis()
        val checkInTime = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.getDefault()
        ).parse(checkInDate)?.time
        val checkOutTime = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.getDefault()
        ).parse(checkOutDate)?.time
        return when {
            currentTime < checkInTime!! -> "Future"
            currentTime in checkInTime..checkOutTime!! -> "Current"
            else -> "Past"
        }
    }

    if (booking.isEmpty()) {
        Text("No Bookings", color = light)
    } else {
        Column() {
            booking.forEach { it ->
                ListItem(
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent,
                    ),
                    headlineContent = {
                        Text(
                            "${formatIsoDateToDDMMYYYY(it.checkIn)} - ${formatIsoDateToDDMMYYYY(it.checkOut)}",
                            color = grayText,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    },
                    supportingContent = {
                        Box(
                            modifier = Modifier
                                .background(
                                    if (getBookingStatus(
                                            it.checkIn,
                                            it.checkOut
                                        ) == "Future"
                                    ) primary
                                    else if (getBookingStatus(
                                            it.checkIn,
                                            it.checkOut
                                        ) == "Current"
                                    ) successBadge
                                    else errorBadge,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 4.dp
                                )
                        ) {
                            Text(
                                getBookingStatus(it.checkIn, it.checkOut),
                                color = Color.Black,
                            )
                        }
                    },
                    trailingContent = {
                        EditDeleteMenu(
                            onEdit = {
                                viewModel.navigate(
                                    ManagerScreen
                                        .ManagerEditBookingScreen
                                        .createRoute(it.id, propertyId)
                                )
                            },
                            onDelete = {
                                viewModel.deleteBooking(it.id)
                            }
                        )
                    },
                    overlineContent = {
                        Text(
                            it.user.name,
                            color = light,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    },
                    )
            }
            Text(
                deleteBookingError ?: "",
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}