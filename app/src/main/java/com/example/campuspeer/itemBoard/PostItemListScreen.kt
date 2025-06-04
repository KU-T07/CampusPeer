package com.example.campuspeer.itemBoard

import androidx.compose.runtime.Composable
import com.example.campuspeer.model.Category
import com.example.campuspeer.model.PostItem

@Composable
fun PostItemListScreen(
    allPosts: List<PostItem>,
    selectedCategory: Category,
    onItemClick: (PostItem) -> Unit
) {
    /*Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "contacts",
            tint = Color.Blue,
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.Center)
        )
    }*/
    val filteredPosts = allPosts.filter { it.category == selectedCategory }

    // 리스트 표시
    PostItemList(
        posts = filteredPosts,
        { post ->
            // 게시글 상세로 이동 등 처리
        }

    )



}