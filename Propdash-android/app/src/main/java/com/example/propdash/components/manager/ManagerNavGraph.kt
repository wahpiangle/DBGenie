package com.example.propdash.components.manager

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.propdash.data.model.User
import com.example.propdash.viewModel.manager.ManagerViewModel

sealed class ManagerScreen(val route: String) {
    object ManagerPropertyScreen : ManagerScreen("manager_property_screen")
    object ManagerMaintenanceScreen : ManagerScreen("manager_maintenance_screen")
    object ManagerCreatePropertyScreen: ManagerScreen("manager_property_create_screen")
}
@Composable
fun ManagerNavGraph(userSession: User?){
    val navController = rememberNavController()
    val managerViewModel = ManagerViewModel(userSession!!)
    NavHost(
        navController = navController,
        startDestination = ManagerScreen.ManagerPropertyScreen.route
    ){
        composable(ManagerScreen.ManagerPropertyScreen.route){
            ManagerPropertyScreen(
                navigate = { route ->
                    navController.navigate(route)
                },
                viewModel = managerViewModel
            )
        }
        composable(ManagerScreen.ManagerMaintenanceScreen.route){
            ManagerMaintenanceScreen(
                navigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable(ManagerScreen.ManagerCreatePropertyScreen.route){
            ManagerCreatePropertyScreen(
                navigate = { route ->
                    navController.navigate(route)
                },
                viewModel = managerViewModel
            )
        }
    }
}