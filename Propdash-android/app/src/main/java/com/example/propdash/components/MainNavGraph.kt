package com.example.propdash.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.propdash.components.manager.ManagerNavGraph
import com.example.propdash.components.shared.LoginScreen
import com.example.propdash.components.shared.RegisterScreen
import com.example.propdash.components.shared.VerificationScreen
import com.example.propdash.components.tenant.TenantNavGraph
import com.example.propdash.data.model.Role
import com.example.propdash.viewModel.UserViewModel

sealed class Screen(val route: String) {
    object TenantScreen : Screen("tenant_screen")
    object ManagerScreen : Screen("manager_screen")
    object LoginScreen : Screen("login_screen")
    object RegisterScreen : Screen("register_screen")
    object VerificationScreen : Screen("verification_screen")
}

@Composable
fun MainNavGraph(userViewModel: UserViewModel) {
    val navController = rememberNavController()
    val userSession = userViewModel.userSession.collectAsState().value
    fun clearSession() {
        userViewModel.clearSession()
    }
    NavHost(
        navController = navController,
        startDestination =
        if (userSession != null) {
            when {
                !userSession.verified -> Screen.VerificationScreen.route
                userSession.role == Role.MANAGER -> Screen.ManagerScreen.route
                else -> Screen.TenantScreen.route
            }
        } else {
            Screen.LoginScreen.route
        }
    ) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                login = { email, password ->
                    userViewModel.login(email, password)
                    if (userSession != null) {
                        navController.navigate(
                            if (userSession.role == Role.MANAGER) {
                                Screen.ManagerScreen.route
                            } else {
                                Screen.TenantScreen.route
                            }
                        )
                    }
                },
                errorMessage = userViewModel.errorMessage.value,
                navigateToRegister = {
                    navController.navigate(Screen.RegisterScreen.route)
                    userViewModel.clearErrorMessage()

                }
            )
        }
        composable(Screen.RegisterScreen.route) {
            RegisterScreen(
                register = { name, email, phoneNumber, password, role ->
                    userViewModel.clearErrorMessage()
                    userViewModel.register(name, email, phoneNumber, password, role)
                    if (userSession != null) {
                        if (!userSession.verified) {
                            navController.navigate(Screen.VerificationScreen.route)
                        }
                        navController.navigate(
                            if (userSession.role == Role.MANAGER) {
                                Screen.ManagerScreen.route
                            } else {
                                Screen.TenantScreen.route
                            }
                        )
                    }
                },
                errorMessage = userViewModel.errorMessage.value,
                navigateToLogin = {
                    navController.navigate(Screen.LoginScreen.route)
                    userViewModel.clearErrorMessage()
                },
            )
        }
        composable(Screen.TenantScreen.route) {
            TenantNavGraph(userSession) {
                userViewModel.clearSession()
                navController.navigate(Screen.LoginScreen.route) {
                    popUpTo(Screen.TenantScreen.route) { inclusive = true }
                }
            }
        }
        composable(Screen.VerificationScreen.route) {
            VerificationScreen(
                verify = { code ->
                    userViewModel.verifyAccount(code)
                    if (userSession != null && userSession.verified) {
                        navController.navigate(
                            if (userSession.role == Role.MANAGER) {
                                Screen.ManagerScreen.route
                            } else {
                                Screen.TenantScreen.route
                            }
                        )
                    }
                },
                errorMessage = userViewModel.errorMessage.value
            )
        }
        composable(Screen.ManagerScreen.route) {
            ManagerNavGraph(userSession, clearSession = { clearSession() },)
        }

    }
}
