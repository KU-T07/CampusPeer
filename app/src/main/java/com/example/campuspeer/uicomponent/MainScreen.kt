package com.example.campuspeer.uicomponenti

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.campuspeer.LocalEmailAuthViewModelOwner
import com.example.campuspeer.itemBoard.PostItemViewModel
import com.example.campuspeer.model.Routes
import com.example.campuspeer.navigation.BottomNavigationBar
import com.example.campuspeer.navigation.NaviGraph
import com.example.campuspeer.ui.theme.Pretendard
import com.example.campuspeer.viewmodel.EmailAuthViewModel

@Composable
fun rememberViewModelStoreOwner(): ViewModelStoreOwner {
    val context = LocalContext.current
    return remember(context) { context as ViewModelStoreOwner }
}

val LocalNavGraphViewModelStoreOwner =
    staticCompositionLocalOf<ViewModelStoreOwner> {
        error("Undefined")
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navStoreOwner = rememberViewModelStoreOwner()
    val navController = rememberNavController()

    val postViewModel: PostItemViewModel = viewModel()
    val posts by postViewModel.posts.collectAsState()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute by remember(backStackEntry) {
        derivedStateOf {
            backStackEntry?.destination?.route?.let {
                Routes.getRoutes(it)
            } ?: run {
                Routes.Main
            }
        }
    }
    val owner: ViewModelStoreOwner = LocalEmailAuthViewModelOwner.current
    val viewModel: EmailAuthViewModel = viewModel(viewModelStoreOwner = owner)



    CompositionLocalProvider(
        LocalNavGraphViewModelStoreOwner provides navStoreOwner
    ) {
        Scaffold(
            topBar = {
                if (currentRoute.isRoot)
                    TopAppBar(
                        title = {
                            Text(
                                text = currentRoute.route, style = TextStyle(
                                    fontFamily = Pretendard,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 32.sp
                                )
                            )
                        },

                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFF85C687),
                            titleContentColor = Color.White     // 타이틀 색
                        )
                    )
            },
            bottomBar = {
                if (currentRoute.isRoot)
                    BottomNavigationBar(navController)
            },
            floatingActionButton = {
                if (currentRoute == Routes.PostItemList)
                    FloatingActionButton(onClick = {
                        navController.navigate(Routes.PostItemCreate.route)
                    },
                        containerColor = Color(0xFF006747),  // 예: 건국대 녹색
                        contentColor = Color.White          // 아이콘 색상
                        ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "")
                    }
            }
        ) { contentPadding ->
            Column(modifier = Modifier.padding(contentPadding)) {
                NaviGraph(
                    navController = navController,
                    currentUserId = viewModel.getCurrentUserId().toString(),
                    startRoute = Routes.PostItemList.route,
                    allPosts = posts
                )
                Text(text = viewModel.getCurrentUserId().toString())
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}