package com.example.propdash.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.example.propdash.R
import com.example.propdash.data.model.Role
import com.example.propdash.data.model.User
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.grayText
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary

@Composable
fun LoginScreen(
    onLoginSuccess: (String, String, String, Role, Boolean, String) -> Unit, userSession: User?,
    login: (String, String) -> Unit,
    errorMessage: String
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding()
            .fillMaxSize()
            .background(color = dark)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.propdash),
            contentDescription = "Logo",
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.house),
            contentDescription = "House",
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            "Welcome Back!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = light,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Text(
            "Login to your existing account",
            style = MaterialTheme.typography.bodyLarge,
            color = grayText,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
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
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
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
        // Display error message if login fails
        Text(
            errorMessage,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Red,
            modifier = Modifier.padding(top = 8.dp)
        )
        Button(
            onClick = {
                 login(email, password)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = primary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Log In")
        }
    }
}
