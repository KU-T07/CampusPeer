package com.example.campuspeer.model

data class PostItem(
    val id: String = "",
    val title: String = "",
    val price: Int = 0,
    val description: String = "",
    val imageUrl: String = "",
    val category: Category = Category.ETC,
    val status: String = "거래가능", // 예약중, 거래완료
    val sellerId: String = "",
    val timestamp: Long = 0L,
    val location: String = ""
)