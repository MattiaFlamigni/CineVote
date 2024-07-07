package com.example.cinevote.badge



import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun BadgeScreen(navController : NavHostController, status : BadgeStatus, action: BadgeAction) {

    LazyColumn(modifier = Modifier.background(MaterialTheme.colorScheme.background)){
        items(status.badgeList) { badge ->
            action.checkBadgeReached(badge)
            BadgeItem(badge = badge, status)
        }
    }
}
@Composable
fun BadgeItem(badge: Badge, status: BadgeStatus) {
    val isAchieved = status.achievedBadges.contains(badge)
    Log.d("badge", isAchieved.toString())

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { /* Azione da eseguire al click sul badge */ },
        colors = CardDefaults.cardColors(
            containerColor = if (isAchieved) Color.Green.copy(alpha = 0.1f)
            else Color.LightGray
        )

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = badge.image),
                contentDescription = badge.name,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = badge.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = badge.description, fontSize = 16.sp)
                Text(text = "Condizione: ${badge.condition}", fontSize = 14.sp)
                badge.effect?.let {
                    Text(text = "Effetto: $it", fontSize = 14.sp)
                }
                if (isAchieved) {
                    Text(
                        text = "Raggiunto!",
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
