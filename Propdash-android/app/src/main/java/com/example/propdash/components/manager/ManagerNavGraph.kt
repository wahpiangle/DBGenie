package com.example.propdash.components.manager

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.propdash.components.manager.propertyDetails.CreateBookingScreen
import com.example.propdash.components.manager.propertyDetails.EditBookingScreen
import com.example.propdash.components.manager.propertyDetails.ManagerPropertyEditScreen
import com.example.propdash.data.model.User
import com.example.propdash.viewModel.manager.ManagerBookingViewModel
import com.example.propdash.viewModel.manager.ManagerCreatePropertyViewModel
import com.example.propdash.viewModel.manager.ManagerMaintenanceRequestDetailViewModel
import com.example.propdash.viewModel.manager.ManagerMaintenanceRequestViewModel
import com.example.propdash.viewModel.manager.ManagerPropertyDetailViewModel
import com.example.propdash.viewModel.manager.ManagerPropertyViewModel

sealed class ManagerScreen(val route: String) {
    data object ManagerPropertyScreen : ManagerScreen("manager_property_screen")
    data object ManagerMaintenanceScreen : ManagerScreen("manager_maintenance_screen")
    data object ManagerCreatePropertyScreen : ManagerScreen("manager_property_create_screen")
    data object ManagerPropertyDetailScreen :
        ManagerScreen("manager_property_detail_screen/{propertyId}") {
        fun createRoute(propertyId: String) = "manager_property_detail_screen/$propertyId"
    }
    data object ManagerPropertyEditScreen :
        ManagerScreen("manager_property_edit_screen/{propertyId}") {
        fun createRoute(propertyId: String) = "manager_property_edit_screen/$propertyId"
    }
    data object CreateBookingScreen:
        ManagerScreen("create_booking_screen/{propertyId}") {
        fun createRoute(propertyId: String) = "create_booking_screen/$propertyId"
    }
    data object ManagerEditBookingScreen:
        ManagerScreen("manager_edit_booking_screen/{bookingId}?propertyId={propertyId}") {
        fun createRoute(bookingId: String, propertyId: String) = "manager_edit_booking_screen/$bookingId?propertyId=$propertyId"
    }

    data object ManagerMaintenanceDetailScreen:
        ManagerScreen("manager_maintenance_detail_screen/{maintenanceId}") {
        fun createRoute(maintenanceId: String) = "manager_maintenance_detail_screen/$maintenanceId"
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
                },
                viewModel = ManagerMaintenanceRequestViewModel(
                    userSession,
                    navigate = { route ->
                        navController.navigate(route)
                    }
                )
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

        composable(
            route = ManagerScreen.ManagerPropertyEditScreen.route,
            arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: return@composable
            ManagerPropertyEditScreen(
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

        composable(
            route = ManagerScreen.CreateBookingScreen.route,
            arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: return@composable
            CreateBookingScreen(
                propertyId = propertyId,
                navigate = { route ->
                    navController.navigate(route)
                },
                viewModel = ManagerBookingViewModel(
                    userSession,
                    navigate = { route ->
                        navController.navigate(route)
                    },
                )
            )
        }

        composable(
            route = ManagerScreen.ManagerEditBookingScreen.route,
            arguments = listOf(
                navArgument("bookingId") { type = NavType.StringType },
                navArgument("propertyId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: return@composable
            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: return@composable
            EditBookingScreen(
                navigate = { route ->
                    navController.navigate(route)
                },
                propertyId = propertyId,
                viewModel = ManagerBookingViewModel(
                    userSession,
                    navigate = { route ->
                        navController.navigate(route)
                    },
                    bookingId = bookingId
                )
            )
        }

        composable(
            route = ManagerScreen.ManagerMaintenanceDetailScreen.route,
            arguments = listOf(navArgument("maintenanceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val maintenanceId = backStackEntry.arguments?.getString("maintenanceId") ?: return@composable
            ManagerMaintenanceDetailScreen(
                navigate = { route ->
                    navController.navigate(route)
                },
                viewModel = ManagerMaintenanceRequestDetailViewModel(
                    userSession,
                    navigate = { route ->
                        navController.navigate(route)
                    },
                    maintenanceId
                ),
            )
        }
    }
}