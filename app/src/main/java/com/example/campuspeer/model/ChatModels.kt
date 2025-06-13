package com.example.campuspeer.model

data class Message(
    val senderId: String = "",
    val text: String? = "",
    val timestamp: Long = 0L,
    val profileImageUrl: String? = ""
)

data class ChatRoom(
    val id: String = "",
    val itemId: String = "",
    val user1Id: String ="", //판매자
    val user2Id: String = "", //구매자
    val participants: List<String> = emptyList()
)