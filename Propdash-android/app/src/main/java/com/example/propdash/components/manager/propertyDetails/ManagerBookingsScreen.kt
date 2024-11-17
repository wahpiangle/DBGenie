package com.example.propdash.components.manager.propertyDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.propdash.viewModel.manager.ManagerCreateBookingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ManagerBookingsScreen(
    booking: List<Booking>,
    viewModel: ManagerCreateBookingViewModel,
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
                                 viewModel.navigate(ManagerScreen.ManagerEditBookingScreen.createRoute(it.id))
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
        }
    }
}