package com.example.campuspeer.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuspeer.model.Message
import com.example.campuspeer.model.PostItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.jvm.java

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _status = MutableStateFlow("거래 진행")
    val status: StateFlow<String> = _status.asStateFlow()

    private val _currentItem = MutableStateFlow<PostItem?>(null) // 현재 거래 물건 정보를 담을 StateFlow
    val currentItem: StateFlow<PostItem?> = _currentItem.asStateFlow()

    private val db = FirebaseFirestore.getInstance()

    fun listenForMessage(roomId: String) {
        db.collection("chatRooms").document(roomId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    println("Listen failed: ${e.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val messagesList = snapshot.documents.mapNotNull { it.toObject(Message::class.java) }
                    _messages.value = messagesList
                }
            }
    }

    fun sendMessage(roomId: String, senderId: String, text: String) {
        val message = Message(
            senderId = senderId,
            text = text,
            timestamp = System.currentTimeMillis()
            // profileImageUrl은 여기서는 직접 전달하지 않음 (ChatBubble에서 senderId 기반으로 처리 가능)
        )
        db.collection("chatRooms").document(roomId)
            .collection("messages")
            .add(message)
            .addOnSuccessListener { println("Message sent!") }
            .addOnFailureListener { e -> println("Error sending message: ${e.message}") }
    }

    fun listenForTransactionStatus(roomId: String) {
        db.collection("chatRooms").document(roomId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    println("Listen for status failed: ${e.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val currentStatus = snapshot.getString("status") ?: "거래 진행"
                    _status.value = currentStatus
                }
            }
    }

    fun updateTransactionStatus(roomId: String, newStatus: String) {
        db.collection("chatRooms").document(roomId)
            .update("status", newStatus)
            .addOnSuccessListener { println("Status updated to $newStatus") }
            .addOnFailureListener { e -> println("Error updating status: ${e.message}") }
    }

    fun fetchItemDetails(itemId: String) {
        viewModelScope.launch {
            try {
                val itemDocument = db.collection("items").document(itemId).get().await()
                if (itemDocument.exists()) {
                    _currentItem.value = itemDocument.toObject(PostItem::class.java)
                } else {
                    _currentItem.value = null // 물건을 찾을 수 없는 경우
                }
            } catch (e: Exception) {
                println("Error fetching item details: ${e.message}")
                _currentItem.value = null
            }
        }
    }
}