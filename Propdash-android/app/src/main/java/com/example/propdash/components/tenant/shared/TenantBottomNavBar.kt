package com.example.propdash.components.tenant.shared

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.propdash.components.tenant.TenantGraph
import com.example.propdash.ui.theme.darkSecondary
import com.example.propdash.ui.theme.primary

@Composable
fun TenantBottomNavBar(currentRoute: String?, navigate: (String) -> Unit) {
    NavigationBar(
        containerColor = darkSecondary,
        ) {
        NavigationBarItem(
            selected = currentRoute == TenantGraph.TenantBookingsScreen.route,
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
            label = { Text("Bookings") },
            onClick = { navigate(
                TenantGraph.TenantBookingsScreen.route
            ) }
        )
        NavigationBarItem(
            selected = currentRoute == TenantGraph.TenantMaintenanceScreen.route,
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
            onClick = { navigate(
                TenantGraph.TenantMaintenanceScreen.route
            ) }
        )
    }
}