package com.example.propdash.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.propdash.R
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.grayText
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary

@Composable
fun VerificationScreen(
    verify: (String) -> Unit,
    errorMessage: String
) {
    var token by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding()
            .fillMaxSize()
            .background(color = dark)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(id = R.drawable.verify),
            contentDescription = "Verify",
        )
        Text(
            text = "Verify your email",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text="A one-time verification code has been sent to your email. Please enter the code below.",
            color = Color.Gray,
            textAlign = TextAlign.Center,
        )
        OutlinedTextField(
            value = token,
            onValueChange = { token = it },
            label = { Text("OTP") },
            textStyle = TextStyle(color = light),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primary,
                unfocusedBorderColor = primary,
                cursorColor = light,
                unfocusedLabelColor = grayText,
                focusedLabelColor = primary,
            ),
        )
        Text(
            text = errorMessage,
            color = Color.Red,
        )
        Spacer(
            modifier = Modifier.size(16.dp)
        )
        Button(
            onClick = {
                verify(token)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = primary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Verify")
        }
    }
}