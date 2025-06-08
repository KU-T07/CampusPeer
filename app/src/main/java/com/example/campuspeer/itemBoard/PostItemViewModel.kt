package com.example.campuspeer.itemBoard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuspeer.model.PostItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PostItemViewModel: ViewModel(){
    private val repo = PostItemRepository()
    val posts = repo.getPosts().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList<PostItem>())

    init {
        viewModelScope.launch {
            posts.collect {
                Log.d("PostViewModel", "받은 posts 개수: ${it.size}")
            }
        }
    }
}