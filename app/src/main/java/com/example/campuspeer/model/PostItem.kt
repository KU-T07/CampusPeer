package com.example.campuspeer.model

import com.google.firebase.database.IgnoreExtraProperties
import com.naver.maps.geometry.LatLng

@IgnoreExtraProperties
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
    val location: String = "",
    val latlng :LatLng = LatLng(37.54168, 127.07867)

)

fun PostItem.toMap(): Map<String, Any> = mapOf(
    "id" to id,
    "title" to title,
    "price" to price,
    "description" to description,
    "category" to category.name,
    "imageUrl" to imageUrl,
    "status" to status,
    "timestamp" to timestamp,
    "sellerId" to sellerId,
    "location" to location,
    "latlng" to latlng
)