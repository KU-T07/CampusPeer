package com.example.campuspeer.chat

data class Message(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0L
)

data class ChatRoom(
    val id: String = "",
    val user1Id: String ="",
    val user2Id: String = ""
)