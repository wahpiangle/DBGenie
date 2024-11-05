package com.example.propdash.viewModel

import androidx.lifecycle.ViewModel
import com.example.propdash.data.model.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel: ViewModel() {
    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _confirmPassword = MutableStateFlow("")
    private val _name = MutableStateFlow("")
    private val _role = MutableStateFlow(Role.TENANT)

    val email = _email.asStateFlow()
    val password = _password.asStateFlow()
    val confirmPassword = _confirmPassword.asStateFlow()
    val name = _name.asStateFlow()
    val role = _role.asStateFlow()

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setConfirmPassword(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
    }

    fun setRole(role: Role) {
        _role.value = role
    }


}