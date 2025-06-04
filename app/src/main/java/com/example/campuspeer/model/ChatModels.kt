package com.example.campuspeer.model

data class Message(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0L,
    val profileImageUrl: String = ""
)

data class ChatRoom(
    val id: String = "",
    val user1Id: String ="",
    val user2Id: String = ""
)