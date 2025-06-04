package com.example.campuspeer.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun ChatBubble(
    text: String,
    isMe: Boolean,
    profileImageUrl: String? = null,
    showProfileImage: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isMe) {
            Box(
                modifier = Modifier
                    .padding(end = 8.dp, start = 4.dp)
                    .size(36.dp) // 항상 공간 확보
            ) {
                if (showProfileImage && !profileImageUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = profileImageUrl,
                        contentDescription = "프로필 이미지",
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(Color.Gray),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(Color.Transparent) // 투명하게만 처리
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .background(
                    color = if (isMe) Color(0xFFD0F0C0) else Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = text,
                style = TextStyle(fontSize = 16.sp)
            )
        }
    }
}

@Preview
@Composable
private fun ChatBubblePreview() {
    Column {
        ChatBubble(
            text = "안녕하세요!",
            isMe = false,
            profileImageUrl = "https://via.placeholder.com/150",
            showProfileImage = true
        )
        ChatBubble(
            text = "연속으로 보냅니다.",
            isMe = false,
            profileImageUrl = "https://via.placeholder.com/150",
            showProfileImage = false
        )
        ChatBubble(
            text = "반갑습니다!",
            isMe = true
        )
    }
}