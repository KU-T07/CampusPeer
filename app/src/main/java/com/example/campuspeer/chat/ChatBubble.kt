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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.campuspeer.model.Message
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatBubble(
    message: Message,
    isMe: Boolean,
    showProfileImage: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isMe) {
            if (showProfileImage) {
                Surface(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    color = Color.LightGray
                ) {
                    if (!message.profileImageUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = message.profileImageUrl,
                            contentDescription = "프로필 이미지",
                            modifier = Modifier.fillMaxSize().aspectRatio(1f),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Default.Person),
                            contentDescription = "기본 프로필 아이콘",
                            modifier = Modifier.fillMaxSize().padding(4.dp),
                            tint = Color.Gray
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
            } else {
                Spacer(modifier = Modifier.width(36.dp + 8.dp))
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
                text = message.text ?: "",
                color = Color.Black,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp)),
            fontSize = 10.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.Bottom)
        )
    }
}

@Preview
@Composable
private fun ChatBubblePreview() {
    Column {
        // 첫 번째 ChatBubble (상대방 메시지, 프로필 이미지 표시)
        ChatBubble(
            message = Message(
                senderId = "userB",
                text = "안녕하세요!",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 5,
                profileImageUrl = "https://via.placeholder.com/36x36/FF5733/FFFFFF?text=P1"
            ),
            isMe = false,
            showProfileImage = true
        )
        // 두 번째 ChatBubble (상대방 메시지, 프로필 이미지 숨김)
        ChatBubble(
            message = Message(
                senderId = "userB",
                text = "연속으로 보냅니다.",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 4,
                profileImageUrl = "https://via.placeholder.com/36x36/FF5733/FFFFFF?text=P1"
            ),
            isMe = false,
            showProfileImage = false
        )
        // 세 번째 ChatBubble (내 메시지)
        ChatBubble(
            message = Message(
                senderId = "userA",
                text = "반갑습니다!",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 3,
                profileImageUrl = null // 내 메시지는 프로필 이미지 없음
            ),
            isMe = true,
            showProfileImage = false
        )
        // 네 번째 ChatBubble (상대방 메시지, 텍스트가 null인 경우)
        ChatBubble(
            message = Message(
                senderId = "userB",
                text = null, // 텍스트가 null인 메시지 테스트
                timestamp = System.currentTimeMillis() - 1000 * 60 * 2,
                profileImageUrl = "https://via.placeholder.com/36x36/FF5733/FFFFFF?text=P1"
            ),
            isMe = false,
            showProfileImage = true
        )
    }
}