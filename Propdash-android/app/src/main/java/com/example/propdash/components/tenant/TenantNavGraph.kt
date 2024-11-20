package com.example.propdash.components.tenant

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.propdash.data.model.User
import com.example.propdash.viewModel.tenant.TenantBookingDetailViewModel
import com.example.propdash.viewModel.tenant.TenantBookingViewModel
import com.example.propdash.viewModel.tenant.TenantMaintenanceDetailViewModel
import com.example.propdash.viewModel.tenant.TenantMaintenanceViewModel

sealed class TenantGraph(val route: String) {
    data object TenantBookingsScreen : TenantGraph("tenant_bookings_screen")
    data object TenantMaintenanceScreen : TenantGraph("tenant_maintenance_screen")
    data object TenantMaintenanceDetailScreen :
        TenantGraph("tenant_maintenance_detail_screen/{maintenanceId}") {
        fun createRoute(maintenanceId: String) = "tenant_maintenance_detail_screen/$maintenanceId"
    }

    data object TenantBookingDetailScreen :
        TenantGraph("tenant_booking_detail_screen/{bookingId}") {
        fun createRoute(bookingId: String) = "tenant_booking_detail_screen/$bookingId"
    }

    data object TenantCreateMaintenanceScreen : TenantGraph("tenant_create_maintenance_screen")
}

@Composable
fun TenantNavGraph(userSession: User?, clearSession: () -> Unit) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = TenantGraph.TenantBookingsScreen.route
    ) {
        composable(TenantGraph.TenantBookingsScreen.route) {
            TenantBookingsScreen(
                viewModel = TenantBookingViewModel(
                    userSession = userSession!!,
                ),
                clearSession = clearSession,
                navigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable(
            route = TenantGraph.TenantBookingDetailScreen.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val bookingId = navBackStackEntry.arguments?.getString("bookingId") ?: return@composable
            TenantBookingDetailScreen(
                navigate = { route ->
                    navController.navigate(route)
                },
                viewModel = TenantBookingDetailViewModel(
                    userSession = userSession!!,
                    bookingId = bookingId
                ),
            )
        }

        composable(TenantGraph.TenantMaintenanceScreen.route) {
            TenantMaintenanceScreen(
                navigate = { route ->
                    navController.navigate(route)
                },
                viewModel = TenantMaintenanceViewModel(
                    userSession = userSession!!,
                    navigate = { route ->
                        navController.navigate(route)
                    }
                )
            )
        }

        composable(
            route = TenantGraph.TenantMaintenanceDetailScreen.route,
            arguments = listOf(navArgument("maintenanceId") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val maintenanceId =
                navBackStackEntry.arguments?.getString("maintenanceId") ?: return@composable
            TenantMaintenanceDetailScreen(
                maintenanceId = maintenanceId,
                viewModel = TenantMaintenanceDetailViewModel(
                    maintenanceId = maintenanceId,
                    userSession = userSession!!
                ),
                navigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable (TenantGraph.TenantCreateMaintenanceScreen.route){
            TenantCreateMaintenanceScreen(
                navigate = { route ->
                    navController.navigate(route)
                },
                viewModel = TenantMaintenanceViewModel(
                    userSession = userSession!!,
                    navigate = { route ->
                        navController.navigate(route)
                    }
                )
            )
        }

    }
}