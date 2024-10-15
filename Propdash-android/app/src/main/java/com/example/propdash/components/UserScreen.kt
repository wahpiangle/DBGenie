package com.example.propdash.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.propdash.Greeting
import com.example.propdash.data.model.User
import com.example.propdash.viewModel.UserViewModel

@Composable
fun UserScreen(userViewModel: UserViewModel = viewModel()) {
    val users by userViewModel.userState.collectAsState()
    val isLoading by userViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.fetchUsers()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(innerPadding))
        } else {
            UserList(users)
        }
    }

}
@Composable
fun UserList(users: List<User>) {
    LazyColumn {
        items(users) { user ->
            UserItem(user)
        }
    }
}

@Composable
fun UserItem(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column() {
            Text(text = user.name)
            Text(text = user.email)
        }
    }
}
