package com.example.propdash.components.tenant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.propdash.components.manager.ManagerScreen
import com.example.propdash.components.tenant.shared.TenantBottomNavBar
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantMaintenanceScreen(
    navigate: (String) -> Unit
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Maintenance Requests",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = dark,
                    titleContentColor = light,
                    navigationIconContentColor = light,
                ),
            )
        },
        bottomBar = {
            TenantBottomNavBar (
                currentRoute = TenantGraph.TenantMaintenanceScreen.route,
                navigate = navigate
            )
        },
        floatingActionButton = {
            Button(
                onClick = {
                    navigate(TenantGraph.TenantCreateMaintenanceScreen.route)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary,
                ),
                modifier = Modifier.size(56.dp)
            ) {
                Text(text = "+")
            }
        },
        containerColor = dark
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Text(
                text = "Maintenance Screen",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}