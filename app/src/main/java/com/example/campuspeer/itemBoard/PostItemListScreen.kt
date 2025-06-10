package com.example.campuspeer.itemBoard

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.campuspeer.model.Category
import com.example.campuspeer.model.PostItem
import com.example.campuspeer.model.Routes
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun PostItemListScreen(
    allPosts: List<PostItem>,
    selectedCategory: Category,
    navController: NavController// 기본값 추가
) {
    val filteredPosts = allPosts

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        if (filteredPosts.isEmpty()) {
            CircularProgressIndicator()
        } else {
            PostItemList(posts = filteredPosts, onClick = { post ->
                navController.navigate(Routes.PostItemDetail.routeWithId(post.id))
            })
        }
    }

    Log.d("PostItemListScreen", "전체 post 수: ${allPosts.size}")
}

@Composable
fun LoadPostAndNavigateDetail(
    postId: String,
    post: PostItem?,
    navController: NavController
) {
    post?.let {
        PostItemDetailScreen(
            initialPost = post,
            onBackClick = { navController.popBackStack() },
            onChatClick = { /* 채팅으로 이동 처리 */ }
        )
    }
}