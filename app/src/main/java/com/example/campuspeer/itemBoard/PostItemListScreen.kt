package com.example.campuspeer.itemBoard

import android.widget.Toast
import androidx.activity.compose.BackHandler
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.campuspeer.model.Category
import com.example.campuspeer.model.PostItem
import com.example.campuspeer.model.Routes
import com.example.campuspeer.model.UserData
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItemListScreen(
    allPosts: List<PostItem>,
    selectedCategory: Category,
    allUsers: List<UserData>,
    navController: NavController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    var selectedCategoryFilter by remember { mutableStateOf<Category?>(null) }
    var departmentQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    var backPressedOnce by remember { mutableStateOf(false) }

    val filteredPosts = allPosts.filter { post ->
        val user = allUsers.find { it.uid == post.sellerId }

        val categoryMatches = selectedCategoryFilter == null || post.category == selectedCategoryFilter

        val normalizedQuery = departmentQuery.trim().lowercase()
        val normalizedDepartment = user?.department?.trim()?.lowercase()
        val departmentMatches = normalizedQuery.isBlank() ||
                (normalizedDepartment?.contains(normalizedQuery) == true)

        categoryMatches && departmentMatches
    }

    // ⏪ 뒤로가기 두 번 클릭 시 앱 종료 처리
    BackHandler {
        if (drawerState.isOpen) {
            coroutineScope.launch { drawerState.close() }
        } else if (!backPressedOnce) {
            backPressedOnce = true
            Toast.makeText(context, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()

            coroutineScope.launch {
                kotlinx.coroutines.delay(2000)
                backPressedOnce = false
            }
        } else {
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(top = 32.dp)) {
                    Text("\uD83D\uDD0D 필터", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)

                    var expanded by remember { mutableStateOf(false) }
                    val categories = listOf<Category?>(null) + Category.entries

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        OutlinedTextField(
                            value = selectedCategoryFilter?.label ?: "전체",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("카테고리") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category?.label ?: "전체") },
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
                        label = { Text("학과 검색") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        coroutineScope.launch { drawerState.open() }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "메뉴 열기",
                            tint = Color.Unspecified
                        )
                    }
                }

                when {
                    allPosts.isEmpty() -> CircularProgressIndicator()
                    filteredPosts.isEmpty() -> Text("게시글이 없습니다.")
                    else -> PostItemList(posts = filteredPosts, onClick = { post ->
                        navController.navigate(Routes.PostItemDetail.routeWithId(post.id))
                    })
                }

                Log.d("PostItemListScreen", "전체 post 수: ${allPosts.size}")
            }
        }
    }
}

@Composable
fun LoadPostAndNavigateDetail(
    postId: String,
    post: PostItem?,
    navController: NavHostController
) {
    post?.let { selectedPost ->
        PostItemDetailScreen(
            navController = navController,
            initialPost = selectedPost,
            onBackClick = { navController.popBackStack() }
        )
    }
}
