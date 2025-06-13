package com.example.campuspeer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.campuspeer.chat.ChatRoomListScreen
import com.example.campuspeer.chat.ChatRoomScreen
import com.example.campuspeer.helpBoard.HelpBoardScreen
import com.example.campuspeer.itemBoard.LoadPostAndNavigateDetail
import com.example.campuspeer.itemBoard.PostItemCreateScreen
import com.example.campuspeer.itemBoard.PostItemListScreen
import com.example.campuspeer.itemBoard.PostItemViewModel
import com.example.campuspeer.model.Category
import com.example.campuspeer.model.PostItem
import com.example.campuspeer.model.Routes
import com.example.campuspeer.model.UserData
import com.example.campuspeer.profile.ProfileScreen
import com.example.campuspeer.uicomponent.EmailAuthScreen
import com.example.campuspeer.uicomponent.LoginScreen
import com.example.campuspeer.uicomponenti.MainScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


@Composable
fun NaviGraph(navController: NavHostController,
              startRoute: String,
              currentUserId: String,
              allPosts: List<PostItem> = emptyList(),
              allUsers: List<UserData> = emptyList()
) {

    val viewModel: PostItemViewModel = viewModel()
    val posts by viewModel.posts.collectAsState()
    val users by viewModel.users.collectAsState()

    NavHost(navController = navController, startDestination = startRoute) {

        composable(Routes.Login.route) {
            LoginScreen(
                onNavigateToMain = { userId ->
                    navController.navigate(Routes.Main.route + "/$userId")
                },
                onRegisterNavigate = {
                    navController.navigate(Routes.Register.route)
                }
            )
        }

        composable(Routes.Register.route) {
            EmailAuthScreen(
                onNavigateToLogin = {
                    navController.popBackStack() // 뒤로가기
                }
            )
        }
        composable(
            route = Routes.Main.route + "/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            MainScreen()
        }

        // ———— BottomNavigationBar에서 사용할 경로들을 추가 등록 ————
        composable(Routes.PostItemList.route) {
            // ex) PostItemListScreen 컴포저블 호출 (userId를 사용해야 한다면 파라미터로 넘기세요)
            val dummyPosts = listOf<PostItem>()
            val defaultCategory = Category.ETC

            PostItemListScreen(
                allPosts = posts,
                selectedCategory = Category.ETC,
                allUsers = users,
                navController = navController
                //onItemClick = { /* 필요 시 상세 이동 로직 */ }
            )
        }

        composable(Routes.Chat.route) {
            ChatRoomListScreen(
                currentUserId = Firebase.auth.currentUser!!.uid,
                onNavigateToChat = { roomId, partnerId, itemId ->
                    navController.navigate(Routes.ChatRoom.create(roomId, partnerId, itemId))
                }
            )
        }

        composable(
            route = Routes.ChatRoom.route,
            arguments = listOf(
                navArgument("roomId")    { type = NavType.StringType },
                navArgument("partnerId") { type = NavType.StringType },
                navArgument("itemId")    { type = NavType.StringType },
            )
        ) { backStackEntry ->
            ChatRoomScreen(
                roomId        = backStackEntry.arguments!!.getString("roomId")!!,
                currentUserId = Firebase.auth.currentUser!!.uid,
                partnerId     = backStackEntry.arguments!!.getString("partnerId")!!,
                itemId        = backStackEntry.arguments!!.getString("itemId")!!
            )
        }


        composable(Routes.Profile.route) {
            ProfileScreen(/* 필요 시 userId 전달 */)
        }

        // 만약 HelpBoard도 BottomNavigation에 포함되어 있다면:
        composable(Routes.HelpBoard.route) {
            HelpBoardScreen(/* 필요 시 인자 전달 */)
        }

        // 게시물 등록 추가
        composable(Routes.PostItemCreate.route) {
            PostItemCreateScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }


        // 디테일로 이동 추가
        // 1) NavGraph.kt 에서
        composable(
            route = Routes.PostItemDetail.route,
            arguments = listOf(navArgument("postId"){ type = NavType.StringType })
        ) { backStack ->
            val postId = backStack.arguments!!.getString("postId")!!
            val viewModel: PostItemViewModel = viewModel()
            val posts by viewModel.posts.collectAsState()
            val post = posts.firstOrNull { it.id == postId }

            LoadPostAndNavigateDetail(
                postId        = postId,
                post          = post,
                navController = navController    // NavHostController 여기를 그대로 넘겨줌
            )
        }




    }
}
