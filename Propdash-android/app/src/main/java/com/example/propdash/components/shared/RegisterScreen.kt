package com.example.propdash.components.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.propdash.data.model.Role
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.grayText
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary
import com.example.propdash.viewModel.RegisterViewModel

@Composable
fun RegisterScreen(
    register: (String, String, String, String, Role) -> Unit,
    errorMessage: String,
    navigateToLogin: () -> Unit,
) {
    val viewModel: RegisterViewModel = viewModel()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val name by viewModel.name.collectAsState()
    val role by viewModel.role.collectAsState()
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding()
            .fillMaxSize()
            .background(color = dark)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Let's get Started!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = light,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Text(
            "Create an account on Propdash",
            style = MaterialTheme.typography.bodyLarge,
            color = grayText,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = name,
            onValueChange = { viewModel.setName(it) },
            label = { Text("Name") },
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
            value = email,
            onValueChange = { viewModel.setEmail(it) },
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
            onValueChange = { viewModel.setPassword(it) },
            label = { Text("Password") },
            textStyle = TextStyle(color = light),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primary,
                unfocusedBorderColor = primary,
                cursorColor = light,
                unfocusedLabelColor = grayText,
                focusedLabelColor = primary,
            ),
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { viewModel.setConfirmPassword(it) },
            label = { Text("Confirm Password") },
            textStyle = TextStyle(color = light),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primary,
                unfocusedBorderColor = primary,
                cursorColor = light,
                unfocusedLabelColor = grayText,
                focusedLabelColor = primary,
            ),
        )
        Text(
            "Select your role",
            style = MaterialTheme.typography.bodySmall,
            color = light,
            modifier = Modifier.padding(top = 16.dp)
        )
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InputChip(
                label = {
                    Text(
                        "Tenant",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                selected = role == Role.TENANT,
                onClick = { viewModel.setRole(Role.TENANT) },
                colors = InputChipDefaults.inputChipColors(
                    selectedContainerColor = primary,
                    selectedLabelColor = light,
                    labelColor = grayText,
                ),
                modifier = Modifier.weight(1f)
            )
            InputChip(
                label = {
                    Text(
                        "Manager",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                selected = role == Role.MANAGER,
                onClick = { viewModel.setRole(Role.MANAGER) },
                colors = InputChipDefaults.inputChipColors(
                    selectedContainerColor = primary,
                    selectedLabelColor = light,
                    labelColor = grayText,
                ),
                modifier = Modifier.weight(1f)
            )
        }
        Text(
            errorMessage,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Red,
            modifier = Modifier.padding(top = 8.dp)
        )
        Button(
            onClick = {
                loading = true
                register(name, email, password, confirmPassword, role)
                loading = false
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = primary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                )
            } else {
                Text("Create Account")
            }
        }
        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                "Already have an account?",
                style = MaterialTheme.typography.bodySmall,
                color = light
            )
            Text(
                "Log In",
                modifier = Modifier.clickable {
                    navigateToLogin()
                },
                style = MaterialTheme.typography.bodySmall,
                color = primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}