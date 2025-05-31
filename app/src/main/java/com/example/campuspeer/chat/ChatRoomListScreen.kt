package com.example.campuspeer.chat

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun ChatRoomListScreen(
    currentUserId: String,
    onNavigateToChat: (String, String) -> Unit
) {
    val db = Firebase.firestore
    var chatRooms by remember { mutableStateOf<List<ChatRoom>>(emptyList()) }

    LaunchedEffect(Unit) {
        db.collection("chatRooms")
            .whereArrayContains("participants", currentUserId)
            .addSnapshotListener { snapshot, _ ->
                chatRooms = snapshot?.map { it.toObject(ChatRoom::class.java) } ?: emptyList()
            }
    }
    LazyColumn {
        items(chatRooms) { room ->
            val partnerId = if (room.user1Id == currentUserId) room.user2Id else room.user1Id
            ListItem(
                headlineContent = { Text("상대방: $partnerId")},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{ onNavigateToChat(room.id, partnerId)}
            )
        }
        Log.d("ChatRoomListScreen", "currentUserId = $currentUserId")
        Log.d("ChatRoomListScreen", "채팅방 수 = ${chatRooms.size}")
    }
}