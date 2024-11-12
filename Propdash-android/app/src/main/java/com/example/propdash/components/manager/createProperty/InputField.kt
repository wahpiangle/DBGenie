package com.example.propdash.components.manager.createProperty

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    restrictToNumber: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = if(restrictToNumber){
                KeyboardType.Number
            } else {
                KeyboardType.Text
            }
        ),
        textStyle = TextStyle(color = light),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = primary,
            unfocusedBorderColor = primary,
            cursorColor = light,
            unfocusedLabelColor = light,
            focusedLabelColor = primary
        ),
        trailingIcon = {
            if (isError) Icon(Icons.Filled.Error, "error", tint = Color.Red)
        },
        supportingText = {
            if (isError) Text(
                if(restrictToNumber && value.isNotEmpty() && value.toDoubleOrNull() == null){
                    "Please enter a valid number"
                } else {
                    "This field is required"
                }
                , color = Color.Red)
        }
    )
}