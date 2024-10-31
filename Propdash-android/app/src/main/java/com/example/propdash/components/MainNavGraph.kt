package com.example.propdash.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.propdash.data.model.Role
import com.example.propdash.viewModel.UserViewModel

sealed class Screen(val route: String) {
    object TenantScreen : Screen("tenant_screen")
    object ManagerScreen: Screen("manager_screen")
    object LoginScreen : Screen("login_screen")
}

@Composable
fun MainNavGraph(userViewModel: UserViewModel) {
    val navController = rememberNavController()
    val userSession = userViewModel.userSession.collectAsState().value
    NavHost(
        navController = navController,
        startDestination = if(userSession != null){
            if(userSession.role == Role.MANAGER) {
                Screen.ManagerScreen.route
            } else{
                Screen.TenantScreen.route
            }
        } else {
            Screen.LoginScreen.route
        }
    ) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                onLoginSuccess = { id, name, email, role, verified, cookie ->
                    userViewModel.saveUserSession(id, name, email, role, verified, cookie)
                    navController.navigate(
                        if(role == Role.MANAGER) {
                            Screen.ManagerScreen.route
                        } else {
                            Screen.TenantScreen.route
                        }
                    )
                },
                login = { email, password -> userViewModel.login(email, password) },
                userSession = userSession,
                errorMessage = userViewModel.errorMessage.value
            )
        }

        composable(Screen.TenantScreen.route) {
            TenantScreen(userSession) {
                userViewModel.clearSession()
                navController.navigate(Screen.LoginScreen.route) {
                    popUpTo(Screen.TenantScreen.route) { inclusive = true }
                }
            }
        }

//        composable("home") {
//            val userSession by userViewModel.userSession.collectAsState()
//            userSession?.let {
//                LoggedInScreen(it) {
//                    // Log out and navigate back to login screen
//                    userViewModel.clearSession()
//                    navController.navigate("login") {
//                        popUpTo("home") { inclusive = true }
//                    }
//                }
//            }
//        }
    }
}
