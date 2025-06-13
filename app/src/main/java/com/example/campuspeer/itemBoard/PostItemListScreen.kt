package com.example.campuspeer.itemBoard

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.campuspeer.model.Category
import com.example.campuspeer.model.PostItem
import com.example.campuspeer.model.Routes

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
    navController: NavHostController    // ① NavHostController 로 변경
) {
    post?.let { selectedPost ->
        PostItemDetailScreen(
            navController = navController,   // ② 빠진 파라미터 추가
            initialPost   = selectedPost,
            onBackClick   = { navController.popBackStack() }
        )
    }
}
