package com.example.propdash.components.manager

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.propdash.data.model.User
import com.example.propdash.viewModel.manager.ManagerCreatePropertyViewModel
import com.example.propdash.viewModel.manager.ManagerPropertyDetailViewModel
import com.example.propdash.viewModel.manager.ManagerPropertyViewModel

sealed class ManagerScreen(val route: String) {
    object ManagerPropertyScreen : ManagerScreen("manager_property_screen")
    object ManagerMaintenanceScreen : ManagerScreen("manager_maintenance_screen")
    object ManagerCreatePropertyScreen : ManagerScreen("manager_property_create_screen")
    object ManagerPropertyDetailScreen :
        ManagerScreen("manager_property_detail_screen/{propertyId}") {
        fun createRoute(propertyId: String) = "manager_property_detail_screen/$propertyId"
    }
    object ManagerPropertyEditScreen :
        ManagerScreen("manager_property_edit_screen/{propertyId}") {
        fun createRoute(propertyId: String) = "manager_property_edit_screen/$propertyId"
    }
}

@Composable
fun ManagerNavGraph(userSession: User?, clearSession: () -> Unit) {
    val navController = rememberNavController()
    val managerCreatePropertyViewModel = ManagerCreatePropertyViewModel(userSession!!,
        navigate = { route ->
            navController.navigate(route)
        })
    val managerPropertyViewModel = ManagerPropertyViewModel(userSession)
    NavHost(
        navController = navController,
        startDestination = ManagerScreen.ManagerPropertyScreen.route
    ) {
        composable(ManagerScreen.ManagerPropertyScreen.route) {
            ManagerPropertyScreen(
                navigate = { route ->
                    navController.navigate(route)
                },
                clearSession,
                viewModel = managerPropertyViewModel
            )
        }
        composable(ManagerScreen.ManagerMaintenanceScreen.route) {
            ManagerMaintenanceScreen(
                navigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable(ManagerScreen.ManagerCreatePropertyScreen.route) {
            ManagerCreatePropertyScreen(
                navigate = { route ->
                    navController.navigate(route)
                },
                viewModel = managerCreatePropertyViewModel
            )
        }
        composable(
            route = ManagerScreen.ManagerPropertyDetailScreen.route,
            arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: return@composable
            ManagerPropertyDetailScreen(
                navigate = {
                    route ->
                    navController.navigate(route)
                    },
                viewModel = ManagerPropertyDetailViewModel(
                    userSession,
                    propertyId,
                    navigate = { route ->
                        navController.navigate(route)
                    }
                )
            )
        }
    }
}