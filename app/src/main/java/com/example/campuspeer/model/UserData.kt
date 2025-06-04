package com.example.campuspeer.model


data class UserData(
    val uid: String = "",
    val email: String = "",
    val department: String = "",
    val studentNumber: String = "",
    val verified: Boolean = false,
    val ratingTotal: Int = 0,
    val ratingCount: Int = 0
)