package com.example.campuspeer.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun sendEmailToAdmin(context: Context, imageUri: Uri, userInfo: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_EMAIL, arrayOf("euichan898@gmail.com"))
        putExtra(Intent.EXTRA_SUBJECT, "학생증 확인 요청")
        putExtra(Intent.EXTRA_TEXT, userInfo)
        putExtra(Intent.EXTRA_STREAM, imageUri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "메일 앱 선택"))
}
