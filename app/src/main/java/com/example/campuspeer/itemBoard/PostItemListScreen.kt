package com.example.campuspeer.itemBoard

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun PostItemListScreen(
    navController: NavController,
    viewModel: PostItemViewModel = viewModel()
) {
    val posts by viewModel.posts.collectAsState()

    LazyColumn {
        items(posts) { post ->
            PostItemList(post = post) {
                navController.navigate("detail/${post.id}")
            }
        }
    }

}