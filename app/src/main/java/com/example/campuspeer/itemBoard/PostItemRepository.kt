package com.example.campuspeer.itemBoard

import com.example.campuspeer.model.PostItem
import com.example.campuspeer.model.toMap
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
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
                val posts = snapshot?.documents?.mapNotNull {
                    it.toObject(PostItem::class.java)?.copy(id = it.id)
                } ?: emptyList<PostItem>()
                trySend(posts)
            }
        awaitClose { subscription.remove() }
    }
}