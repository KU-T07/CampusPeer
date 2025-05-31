package com.example.campuspeer.itemBoard

import com.google.firebase.Timestamp

data class PostItem(
    val id: String = "",
    val title: String = "",
    val price: Int = 0,
    val description: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val status: String = "거래가능", //예약중, 거래완료
    val timestamp: Timestamp = Timestamp.now(),
    val sellerId: String = "",
    val location: String = ""
)
