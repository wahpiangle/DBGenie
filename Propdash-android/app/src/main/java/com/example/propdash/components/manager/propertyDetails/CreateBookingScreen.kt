package com.example.propdash.components.manager.propertyDetails

import DatePickerFieldToModal
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.propdash.viewModel.manager.ManagerCreateBookingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBookingScreen(
    propertyId: String,
    navigate: (String) -> Unit,
    viewModel: ManagerCreateBookingViewModel
) {
    val createBookingError = viewModel.createBookingError.collectAsState()
    var tenantEmail by remember { mutableStateOf("") }
    var tenantEmailError by remember { mutableStateOf<String?>(null) }

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

    fun validate(): Boolean {
        var isValid = true
        if (tenantEmail.isEmpty()) {
            tenantEmailError = "Tenant email is required"
            isValid = false
        } else {
            tenantEmailError = null
        }

        if (checkIn.value == null) {
            checkInError = "Check-in date is required"
            isValid = false
        } else if (checkIn.value!! >= checkOut.value!!) {
            checkInError = "Check-in date cannot be after check-out date"
            isValid = false

        } else {
            checkInError = null
        }

        if (checkOut.value == null) {
            checkOutError = "Check-out date is required"
            isValid = false
        } else if (checkIn.value!! >= checkOut.value!!) {
            checkOutError = "Check-out date cannot be before check-in date"
            isValid = false
        } else {
            checkOutError = null
        }

        if (rentalPrice.value.isEmpty()) {
            rentalPriceError = "Rental price is required"
            isValid = false
        } else {
            rentalPriceError = null
        }

        if (rentCollectionDay.value == null) {
            rentCollectionDayError = "Rent collection day is required"
            isValid = false
        } else {
            rentCollectionDayError = null
        }

        return isValid
    }

    Scaffold(
        containerColor = dark,
        contentColor = dark,
        topBar = {
            TopAppBar(
                title = {
                    Text("Create Booking", color = light)
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
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            InputField(
                value = tenantEmail,
                onValueChange = {
                    tenantEmail = it
                    tenantEmailError = null
                },
                label = "Tenant Email",
                isError = tenantEmailError != null,
            )

            InputField(
                value = remarks.value,
                onValueChange = { remarks.value = it },
                label = "Remarks",
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
                text = createBookingError.value ?: "",
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = primary),
                onClick = {
                    if (validate()) {
                        viewModel.createBooking(
                            propertyId = propertyId,
                            tenantEmail = tenantEmail,
                            checkIn = checkIn.value!!,
                            checkOut = checkOut.value!!,
                            remarks = remarks.value,
                            rentalPrice = rentalPrice.value,
                            rentCollectionDay = rentCollectionDay.value!!,
                        )
                    }
                }
            ) {
                Text(
                    "Create Booking",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

        }
    }
}