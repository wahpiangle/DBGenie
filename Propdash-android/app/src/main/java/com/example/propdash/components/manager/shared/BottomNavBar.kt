package com.example.propdash.components.manager.shared

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.propdash.components.manager.ManagerScreen
import com.example.propdash.ui.theme.darkSecondary
import com.example.propdash.ui.theme.primary

@Composable
fun BottomNavBar(currentRoute: String?, navigate: (String) -> Unit) {
    NavigationBar (
        containerColor = darkSecondary,

    ){
        NavigationBarItem(
            selected = currentRoute == ManagerScreen.ManagerPropertyScreen.route,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = primary,
                selectedTextColor = primary,
                indicatorColor = darkSecondary
            ),
            icon = {
                Icon(
                    Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Properties") },
            onClick = { navigate(ManagerScreen.ManagerPropertyScreen.route) }
        )
        NavigationBarItem(
            selected = currentRoute == ManagerScreen.ManagerMaintenanceScreen.route,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = primary,
                selectedTextColor = primary,
                indicatorColor = darkSecondary
            ),
            icon = {
                Icon(
                    Icons.AutoMirrored.Outlined.List,
                    contentDescription = "Home",
                )
            },
            label = { Text("Maintenance") },
            onClick = { navigate(ManagerScreen.ManagerMaintenanceScreen.route) }
        )
    }
}