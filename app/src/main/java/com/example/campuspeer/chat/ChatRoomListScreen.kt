package com.example.campuspeer.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.campuspeer.model.ChatRoom
import com.example.campuspeer.model.UserData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun ChatRoomListScreen(
    currentUserId: String,
    onNavigateToChat: (roomId: String, partnerId: String, itemId: String) -> Unit
) {
    val db = Firebase.firestore

    var chatRooms by remember { mutableStateOf<List<ChatRoom>>(emptyList()) }
    val displayNames = remember { mutableStateMapOf<String, String>() }
    val itemTitles   = remember { mutableStateMapOf<String, String>() }

    // 1) 내 채팅방 구독
    LaunchedEffect(currentUserId) {
        db.collection("chatRooms")
            .whereArrayContains("participants", currentUserId)
            .addSnapshotListener { snap, _ ->
                chatRooms = snap?.mapNotNull { it.toObject(ChatRoom::class.java) } ?: emptyList()
            }
    }

    // 2) 채팅방이 바뀔 때마다: 파트너 이름과 아이템 타이틀을 한 번만 읽어오기
    LaunchedEffect(chatRooms) {
        chatRooms.forEach { room ->
            val partnerId = room.user1Id.takeIf { it != currentUserId } ?: room.user2Id

            // 파트너 이름
            if (!displayNames.containsKey(partnerId)) {
                db.collection("users").document(partnerId)
                    .get()
                    .addOnSuccessListener { doc ->
                        val user = doc.toObject(UserData::class.java)
                        val name = user?.studentNumber?.takeIf { it.isNotBlank() }
                            ?: user?.email?.substringBefore("@")
                            ?: partnerId
                        displayNames[partnerId] = name
                    }
                    .addOnFailureListener { displayNames[partnerId] = partnerId }
            }

            // 상품 제목
            if (!itemTitles.containsKey(room.itemId)) {
                db.collection("posts").document(room.itemId)
                    .get()
                    .addOnSuccessListener { doc ->
                        // title만 꺼내오기
                        val title = doc.getString("title") ?: "알 수 없는 상품"
                        itemTitles[room.itemId] = title
                     }
            }
        }
    }

    // 3) UI
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(chatRooms) { room ->
            val partnerId  = room.user1Id.takeIf { it != currentUserId } ?: room.user2Id
            val name       = displayNames[partnerId] ?: "로딩 중…"
            val title      = itemTitles[room.itemId] ?: "로딩 중…"
            val labelText  = "$name • $title"

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable { onNavigateToChat(room.id, partnerId, room.itemId) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.elevatedCardColors()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 간단 아바타
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = labelText,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
