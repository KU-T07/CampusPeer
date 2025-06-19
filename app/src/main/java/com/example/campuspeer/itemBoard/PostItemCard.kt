package com.example.campuspeer.itemBoard

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.campuspeer.model.PostItem

import java.util.concurrent.TimeUnit

fun formatTimestampToRelativeTime(timestamp: Long): String {

    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < TimeUnit.MINUTES.toMillis(1) -> "Just now"
        diff < TimeUnit.HOURS.toMillis(1) -> {
            val mins = TimeUnit.MILLISECONDS.toMinutes(diff)
            "Today • ${mins} min"
        }
        diff < TimeUnit.DAYS.toMillis(1) -> {
            val hours = TimeUnit.MILLISECONDS.toHours(diff)
            "Today • ${hours} hour"
        }
        else -> {
            val days = TimeUnit.MILLISECONDS.toDays(diff)
            "$days days ago"
        }
    }
}


@Composable
fun PostItemCard(
    post: PostItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9)) // 직접 지정한 카드 배경색
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Image(
                painter = rememberAsyncImagePainter(post.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(post.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    "₩${post.price}",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF007BFF))
                )
                Text(
                    text = post.description.take(30) + if (post.description.length > 30) "…" else "",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
                Text(
                    text = formatTimestampToRelativeTime(post.timestamp), // 시간은 실제 timestamp 포맷팅 필요
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }

            Text(
                text = post.status,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .background(
                        color = when (post.status) {
                            "거래가능" -> Color(0xFF2ECC71)
                            "예약중" -> Color(0xFF3498DB)
                            "거래완료" -> Color(0xFF9E9E9E)
                            else -> Color.Red
                        },
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostItemListPreview() {
    val dummyPost = PostItem(
        id = "1",
        title = "List item",
        price = 10000,
        description = "Supporting line text lorem ipsum dolor sit amet",
        status = "예약중",
        imageUrl = "https://via.placeholder.com/150",
        timestamp = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1600)
    )
    PostItemCard(post = dummyPost, onClick = {})
}
