package com.example.campuspeer.model

data class PostItem(
    val id: String = "",
    val title: String = "",
    val price: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val category: String = "",
    val status: String = "거래가능", // 예약중, 거래완료
    val sellerId: String = "",
    val timestamp: Long = 0L,
    val location: String = ""
)