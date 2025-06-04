
package com.example.campuspeer.chat

import androidx.lifecycle.ViewModel
import com.example.campuspeer.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class ChatViewModel : ViewModel(){

    val messages = MutableStateFlow<List<Message>>(emptyList())
    private val db = FirebaseFirestore.getInstance()

    private val _status = MutableStateFlow("거래 진행")
    val status: StateFlow<String> = _status

    fun listenForTransactionStatus(roomId: String){
        Firebase.firestore.collection("chatRooms")
            .document(roomId)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val newStatus = snapshot.getString("transactionStatus") ?: "거래 진행"
                _status.value = newStatus
            }
    }

    fun updateTransactionStatus(roomId: String, newStatus: String){
        Firebase.firestore.collection("chatRooms")
            .document(roomId)
            .update("transactionStatus", newStatus)
    }

    fun listenForMessage(roomId: String){
        db.collection("chatRooms").document(roomId).collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, _ ->
                val result = snapshot?.map {it.toObject(Message::class.java)} ?: emptyList()
                messages.value = result
            }
    }


    fun getUserProfileUrl(userId: String): String {
        return when (userId) {
            "T3SDNm5GqYfNSEb8KIqX2aCxFmc2" -> "https://example.com/seller.png"
            else -> "https://example.com/buyer.png"
        }
    }

    fun sendMessage(roomId: String, senderId: String, text: String){
        val profileImageUrl = getUserProfileUrl(senderId)
        val msg = Message(senderId, text, System.currentTimeMillis(), profileImageUrl)

        db.collection("chatRooms").document(roomId)
            .collection("messages").add(msg)
    }

}
