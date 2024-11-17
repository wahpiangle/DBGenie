package com.example.propdash.components.manager.propertyDetails

import DatePickerFieldToModal
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.propdash.components.manager.ManagerScreen
import com.example.propdash.components.manager.createProperty.InputField
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary
import com.example.propdash.viewModel.manager.ManagerBookingViewModel
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookingScreen(
    navigate: (String) -> Unit,
    propertyId: String,
    viewModel: ManagerBookingViewModel
) {
    val booking = viewModel.booking.collectAsState().value
    val editBookingError = viewModel.editBookingError.collectAsState().value
    val remarks = remember(booking) {
        mutableStateOf(booking?.remarks ?: "")
    }
    fun convertISOToMillis(date: String?): Long {
        return date?.let { SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(it)?.time } ?: 0
    }
    val checkIn = remember(booking) { mutableLongStateOf(
        convertISOToMillis(booking?.checkIn)
    ) }
    var checkInError by remember { mutableStateOf<String?>(null) }

    val checkOut = remember(booking) { mutableLongStateOf(
        convertISOToMillis(booking?.checkOut)
    ) }
    var checkOutError by remember { mutableStateOf<String?>(null) }

    val rentalPrice = remember(booking) { mutableStateOf(booking?.rentalPrice ?: "") }
    var rentalPriceError by remember { mutableStateOf<String?>(null) }

    val rentCollectionDay = remember(booking) { mutableStateOf(booking?.rentCollectionDay) }
    var rentCollectionDayError by remember { mutableStateOf<String?>(null) }

    fun updateCheckInDate(date: Long) {
        checkIn.longValue = date
    }

    fun updateCheckOutDate(date: Long) {
        checkOut.longValue = date
    }

    fun validate(): Boolean {
        var isValid = true
        if (checkIn.longValue >= checkOut.longValue) {
            checkInError = "Check-in date cannot be after check-out date"
            isValid = false
        } else {
            checkInError = null
        }

        if (rentalPrice.value.isEmpty()) {
            rentalPriceError = "Rental Price is required"
            isValid = false
        }
        if (rentCollectionDay.value == null) {
            rentCollectionDayError = "Rent Collection Day is required"
            isValid = false
        }
        return isValid
    }

    Scaffold(
        containerColor = dark,
        contentColor = dark,
        topBar = {
            TopAppBar(
                title = {
                    Text("Edit Booking", color = light)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigate(
                            ManagerScreen.ManagerPropertyDetailScreen.createRoute(
                                propertyId
                            )
                        )
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = dark,
                    titleContentColor = light,
                    navigationIconContentColor = light,
                ),
            )
        }
    ) {padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp)
        ){
            if(booking == null){
                CircularProgressIndicator()
                return@Column
            }
            InputField(
                label = "Remarks",
                value = remarks.value,
                onValueChange = { remarks.value = it },
                isError = false,
            )
            DatePickerFieldToModal(
                selectedDate = checkIn.longValue,
                onDateSelected = {
                    updateCheckInDate(it)
                    checkInError = null
                },
                label = "Check In Date",
                isError = checkInError != null,
                errorMessage = checkInError ?: ""
            )
            DatePickerFieldToModal(
                selectedDate = checkOut.longValue,
                onDateSelected = {
                    updateCheckOutDate(it)
                    checkOutError = null
                },
                label = "Check Out Date",
                isError = checkOutError != null,
                errorMessage = checkOutError ?: ""
            )
            InputField(
                value = rentalPrice.value,
                onValueChange = {
                    rentalPrice.value = it
                    rentalPriceError = null
                },
                label = "Rental Price Per Month",
                isError = rentalPriceError != null,
            )
            InputField(
                value = rentCollectionDay.value?.toString() ?: "",
                onValueChange = {
                    rentCollectionDay.value = it.toIntOrNull()
                    rentCollectionDayError = null
                },
                label = "Rent Collection Day",
                isError = rentCollectionDayError != null,
            )
            Text(
                text = editBookingError ?: "",
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = primary),
                onClick = {
                    if (validate()) {
                        viewModel.editBooking(
                            bookingId = booking.id,
                            remarks = remarks.value,
                            checkIn = checkIn.longValue,
                            checkOut = checkOut.longValue,
                            rentalPrice = rentalPrice.value,
                            rentCollectionDay = rentCollectionDay.value!!,
                            propertyId = propertyId
                        )
                    }
                }
            ) {
                Text(
                    "Update Booking",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}