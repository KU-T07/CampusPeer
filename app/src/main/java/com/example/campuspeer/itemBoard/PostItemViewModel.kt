package com.example.campuspeer.itemBoard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuspeer.model.PostItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class PostItemViewModel: ViewModel(){
    private val repo = PostItemRepository()
    val posts = repo.getPosts().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList<PostItem>())
}