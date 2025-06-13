
package com.example.campuspeer.itemBoard

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.campuspeer.model.Category
import com.example.campuspeer.model.PostItem
import com.example.campuspeer.model.Routes
import com.example.campuspeer.model.UserData
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItemListScreen(
    allPosts: List<PostItem>,
    selectedCategory: Category,
    allUsers: List<UserData>,
    navController: NavController
) {
    Log.d("PostItemListScreen", "🔍 넘어온 전체 사용자 수: ${allUsers.size}")
    allUsers.forEach {
        Log.d("PostItemListScreen", "🧑 uid=${it.uid}, department=${it.department}")
    }

    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedCategoryFilter by remember { mutableStateOf<Category?>(null) }
    var departmentQuery by remember { mutableStateOf("") }

    val filteredPosts = allPosts.filter { post ->
        val user = allUsers.find { it.uid == post.sellerId }

        if (user != null) {
            Log.d("학과필터", "✅ 매칭됨: post.sellerId=${post.sellerId} == user.uid=${user.uid}")
        } else {
            Log.d("학과필터", "❌ 매칭 안됨: post.sellerId=${post.sellerId} 와 일치하는 uid 없음")
            Log.d("학과필터", "📌 전체 유저 UID 목록: ${allUsers.map { it.uid }}")
        }

        val categoryMatches = selectedCategoryFilter == null || post.category == selectedCategoryFilter

        val normalizedQuery = departmentQuery.trim().lowercase()
        val normalizedDepartment = user?.department?.trim()?.lowercase()
        val departmentMatches = normalizedQuery.isBlank() ||
                (normalizedDepartment?.contains(normalizedQuery) == true)

        Log.d("학과필터", "🎯 비교: DB=${normalizedDepartment}, 입력=$normalizedQuery, 결과=$departmentMatches")

        categoryMatches && departmentMatches
    }




    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                allPosts.isEmpty() -> CircularProgressIndicator()
                filteredPosts.isEmpty() -> Text("게시글이 없습니다.")
                else -> PostItemList(posts = filteredPosts, onClick = { post ->
                    navController.navigate(Routes.PostItemDetail.routeWithId(post.id))
                })
            }
            Log.d("PostItemListScreen", "전체 post 수: ${allPosts.size}")
        }

        if (showFilterDialog) {
            AlertDialog(
                onDismissRequest = { showFilterDialog = false },
                title = { Text("필터 설정") },
                text = {
                    Column {
                        var expanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = selectedCategoryFilter?.label ?: "카테고리 선택",
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                                modifier = Modifier.menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                Category.entries.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(category.label) },
                                        onClick = {
                                            selectedCategoryFilter = category
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = departmentQuery,
                            onValueChange = { departmentQuery = it },
                            label = { Text("학과 검색") }
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showFilterDialog = false }) {
                        Text("적용")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showFilterDialog = false }) {
                        Text("취소")
                    }
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { showFilterDialog = true }) {
                Text("필터")
            }
        }
    }
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
