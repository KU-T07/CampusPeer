package com.example.campuspeer.itemBoard

import com.example.campuspeer.model.Category
import com.example.campuspeer.model.PostItem
import com.example.campuspeer.model.toMap
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PostItemRepository {
    private val db = Firebase.firestore

    fun addPost(
        post: PostItem,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        val postMap = post.copy(timestamp = System.currentTimeMillis()).toMap()
        db.collection("posts")
            .add(postMap)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getPosts(): Flow<List<PostItem>> = callbackFlow {
        val subscription = db.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                val posts = snapshot?.documents?.mapNotNull { doc ->
                    val categoryStr = doc.getString("category") ?: return@mapNotNull null
                    val category = Category.entries.firstOrNull {
                        it.name.equals(categoryStr, ignoreCase = true)
                    } ?: return@mapNotNull null


                    val latlngMap = doc.get("latlng") as? Map<*, *>
                    val latlng = latlngMap?.let {
                        val lat = it["latitude"] as? Double
                        val lng = it["longitude"] as? Double
                        if (lat != null && lng != null) LatLng(lat, lng)
                        else LatLng(37.54168, 127.07867)
                    } ?: LatLng(37.54168, 127.07867)

                    PostItem(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        price = doc.getLong("price")?.toInt() ?: 0,
                        location = doc.getString("location") ?: "",
                        sellerId = doc.getString("sellerId") ?: "",
                        timestamp = doc.getLong("timestamp") ?: 0L,
                        imageUrl = doc.getString("imageUrl") ?: "",
                        category = category,
                        status = doc.getString("status") ?: "",
                        latlng = latlng,
                    )
                } ?: emptyList()
                trySend(posts)
            }
        awaitClose { subscription.remove() }
    }
}