package com.example.propdash.components.manager.createProperty

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.propdash.components.manager.ManagerScreen
import com.example.propdash.data.model.BookingStatus
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary
import com.example.propdash.ui.theme.successBadge
import com.example.propdash.ui.theme.errorBadge

@Composable
fun PropertyCard(
    imageUrl: String,
    propertyName: String,
    price: String,
    status: BookingStatus,
    navigate: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        onClick = {
            navigate(ManagerScreen.ManagerPropertyDetailScreen.route)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                model = imageUrl,
                contentScale = ContentScale.Fit,
                contentDescription = null,
                onError = { error->
                    Log.e("PropertyCard", "Error loading image: $error")
                },
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(primary)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f).padding(8.dp)
                ) {
                    Text(
                        text = propertyName,
                        style = TextStyle(fontSize = 20.sp,
                            color = light),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = status.name,
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .background(
                                color = when(status) {
                                    BookingStatus.VACANT -> successBadge
                                    BookingStatus.OCCUPIED -> errorBadge
                                },
                                shape = RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
                Text(
                    text = price,
                    style = TextStyle(fontSize = 20.sp,
                        color = light,
                        fontWeight = FontWeight.Bold
                    ),
                )
            }
        }
    }
}
