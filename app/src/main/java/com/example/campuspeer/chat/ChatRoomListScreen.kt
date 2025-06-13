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


    LaunchedEffect(Unit) {
        db.collection("chatRooms")
            .whereArrayContains("participants", currentUserId)
            .addSnapshotListener { snapshot, _ ->
                chatRooms = snapshot?.map { it.toObject(ChatRoom::class.java) } ?: emptyList()
            }
    }
    LaunchedEffect(chatRooms) {
        chatRooms.forEach { room ->
            val partnerId = room.user1Id.takeIf { it != currentUserId } ?: room.user2Id
            if (!displayNames.containsKey(partnerId)) {
                db.collection("users")
                    .document(partnerId)
                    .get()
                    .addOnSuccessListener { doc ->
                        val user = doc.toObject(UserData::class.java)
                        // 학번이 있으면 학번, 없으면 email 앞부분, 없으면 UID
                        val name = user?.studentNumber
                            .takeUnless { it.isNullOrBlank() }
                            ?: user?.email?.substringBefore("@")
                            ?: partnerId
                        displayNames[partnerId] = name
                    }
                    .addOnFailureListener {
                        displayNames[partnerId] = partnerId
                    }
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(chatRooms) { room ->
            // 내 ID가 user1Id면 상대는 user2Id, 아니면 user1Id
            val partnerId = if (room.user1Id == currentUserId) room.user2Id else room.user1Id
            val displayName = displayNames[partnerId] ?: "로딩 중..."

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
                    // 1) 아바타 플레이스홀더
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "avatar",
                            tint = Color.Gray,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // 2) 상대방 ID
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}