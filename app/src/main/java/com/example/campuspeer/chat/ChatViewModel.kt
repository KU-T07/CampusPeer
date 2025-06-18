package com.example.campuspeer.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuspeer.model.Category
import com.example.campuspeer.model.Message
import com.example.campuspeer.model.PostItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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
        val chatRoomRef = db.collection("chatRooms").document(roomId)

        viewModelScope.launch {
            try {
                // 1️⃣ chatRoom 업데이트
                chatRoomRef.update("status", newStatus).await()

                // 2️⃣ chatRoom 문서에서 itemId 가져오기
                val snapshot = chatRoomRef.get().await()
                val itemId = snapshot.getString("itemId")

                // 3️⃣ posts 문서도 업데이트
                if (!itemId.isNullOrBlank()) {
                    db.collection("posts").document(itemId)
                        .update("status", newStatus)
                        .addOnSuccessListener { println("✅ posts/$itemId 상태도 업데이트 완료") }
                        .addOnFailureListener { e -> println("❌ posts/$itemId 업데이트 실패: ${e.message}") }
                }
            } catch (e: Exception) {
                println("❌ 거래 상태 업데이트 중 오류: ${e.message}")
            }
        }

        // UI 반영용
        _status.value = newStatus
    }

    fun fetchItemSummaryForChat(itemId: String) {
        println("🟢 [fetchItemSummaryForChat] - itemId = $itemId")
        viewModelScope.launch {
            try {
                val snapshot = db.collection("posts").document(itemId).get().await()
                if (snapshot.exists()) {
                    val post = PostItem(
                        id = itemId,
                        title = snapshot.getString("title") ?: "",
                        price = snapshot.getLong("price")?.toInt() ?: 0,
                        imageUrl = snapshot.getString("imageUrl") ?: "",
                        status = snapshot.getString("status") ?: "",
                        // 아래는 더미값 or 기본값으로 채워줍니다
                        description = "",
                        category = Category.ETC,
                        sellerId = snapshot.getString("sellerId") ?: "",
                        timestamp = 0L,
                        location = "" // 안 쓸 거라 상관 없음
                    )

                    println("✅ 채팅용 post = $post")
                    _currentItem.value = post
                } else {
                    _currentItem.value = null
                }
            } catch (e: Exception) {
                println("❌ fetchItemSummaryForChat 예외: ${e.message}")
                _currentItem.value = null
            }
        }
    }



}