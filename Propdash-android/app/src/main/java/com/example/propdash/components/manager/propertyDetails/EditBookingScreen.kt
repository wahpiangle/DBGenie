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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookingScreen(
    navigate: (String) -> Unit,
    propertyId: String,
    viewModel: ManagerBookingViewModel
) {
    val booking = viewModel.booking.collectAsState().value
    val editBookingError = viewModel.editBookingError.collectAsState().value
    val remarks = remember { mutableStateOf("") }

    val checkIn = remember { mutableStateOf<Long?>(null) }
    var checkInError by remember { mutableStateOf<String?>(null) }

    val checkOut = remember { mutableStateOf<Long?>(null) }
    var checkOutError by remember { mutableStateOf<String?>(null) }

    val rentalPrice = remember { mutableStateOf("") }
    var rentalPriceError by remember { mutableStateOf<String?>(null) }

    val rentCollectionDay = remember { mutableStateOf<Int?>(null) }
    var rentCollectionDayError by remember { mutableStateOf<String?>(null) }

    fun updateCheckInDate(date: Long) {
        checkIn.value = date
    }

    fun updateCheckOutDate(date: Long) {
        checkOut.value = date
    }
    Log.d("EditBookingScreen", "booking: $booking")
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
            InputField(
                label = "Remarks",
                value = remarks.value,
                onValueChange = { remarks.value = it },
                isError = false,
            )
            DatePickerFieldToModal(
                selectedDate = checkIn.value,
                onDateSelected = {
                    updateCheckInDate(it)
                    checkOutError = null
                },
                label = "Check In Date",
                isError = checkInError != null,
                errorMessage = checkInError ?: ""
            )
            DatePickerFieldToModal(
                selectedDate = checkOut.value,
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
//                    if (validate()) {

//                    }
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