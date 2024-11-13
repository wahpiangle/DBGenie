package com.example.propdash.components.manager.propertyDetails

import DatePickerFieldToModal
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.propdash.components.manager.ManagerScreen
import com.example.propdash.components.manager.createProperty.InputField
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.light
import com.example.propdash.viewModel.manager.ManagerCreateBookingViewModel
import convertMillisToDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBookingScreen(
    propertyId: String,
    navigate: (String) -> Unit,
    viewModel: ManagerCreateBookingViewModel
){
    val createBookingError = viewModel.createBookingError.collectAsState()
    var tenantEmail by remember{ mutableStateOf("") }
    val remarks = remember{ mutableStateOf("") }
    var checkIn = remember{ mutableLongStateOf(System.currentTimeMillis()) }
    var checkOut = remember{ mutableLongStateOf(System.currentTimeMillis()) }
    var rentalPrice = remember{ mutableStateOf("") }
    var rentCollectionDay = remember{ mutableIntStateOf(0) }
    val dateRangePickerState = rememberDateRangePickerState()

    fun updateCheckInDate(date: Long){
        checkIn.longValue = date
    }

    fun updateCheckOutDate(date: Long){
        checkOut.longValue = date
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
                    IconButton(onClick = { navigate(ManagerScreen.ManagerPropertyDetailScreen.createRoute(propertyId)) }) {
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
    ){ padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp)
        ){
            InputField(
                value = tenantEmail,
                onValueChange = { tenantEmail = it },
                label = "Tenant Email",
                isError = createBookingError.value != null,
            )
            InputField(
                value = remarks.value,
                onValueChange = { remarks.value = it },
                label = "Remarks",
                isError = createBookingError.value != null,
            )
            DatePickerFieldToModal(
                selectedDate = checkIn.longValue,
                onDateSelected = { updateCheckInDate(it) }
            )
            DatePickerFieldToModal(
                selectedDate = checkOut.longValue,
                onDateSelected = { updateCheckOutDate(it) }
            )
            InputField(
                value = rentalPrice.value,
                onValueChange = { rentalPrice.value = it },
                label = "Rental Price Per Month",
                isError = createBookingError.value != null,
            )
            InputField(
                value = rentCollectionDay.intValue.toString(),
                onValueChange = { rentCollectionDay.value = it.toIntOrNull() ?: 0 },
                label = "Rent Collection Day",
                isError = createBookingError.value != null,
            )
        }
    }
}