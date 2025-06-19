package com.example.campuspeer.util

import android.util.Log
import com.google.firebase.database.FirebaseDatabase

object RatingUtils {

    fun updateUserRating(userId: String, rating: Int, onComplete: (Boolean) -> Unit) {
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)

        userRef.get().addOnSuccessListener { snapshot ->
            val currentTotal = snapshot.child("ratingTotal").getValue(Int::class.java) ?: 0
            val currentCount = snapshot.child("ratingCount").getValue(Int::class.java) ?: 0

            val newTotal = currentTotal + rating
            val newCount = currentCount + 1

            userRef.child("ratingTotal").setValue(newTotal)
            userRef.child("ratingCount").setValue(newCount)
                .addOnSuccessListener {
                    Log.d("RatingUtils", "✅ 평점 업데이트 완료: ${rating}점")
                    onComplete(true)
                }
                .addOnFailureListener {
                    Log.e("RatingUtils", "❌ 평점 업데이트 실패: ${it.message}")
                    onComplete(false)
                }

        }.addOnFailureListener {
            Log.e("RatingUtils", "❌ 사용자 조회 실패: ${it.message}")
            onComplete(false)
        }
    }

    fun markRatingDone(roomId: String, userId: String) {
        val ref = FirebaseDatabase.getInstance()
            .getReference("RatingsDone")
            .child(roomId)
            .child(userId)

        ref.setValue(true)
    }
}