package com.example.campuspeer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.campuspeer.chat.ChatRoomListScreen
import com.example.campuspeer.chat.ChatRoomScreen
import com.example.campuspeer.helpBoard.HelpBoardScreen
import com.example.campuspeer.itemBoard.PostItemCreateScreen
import com.example.campuspeer.itemBoard.PostItemListScreen
import com.example.campuspeer.model.Category
import com.example.campuspeer.model.PostItem
import com.example.campuspeer.model.Routes
import com.example.campuspeer.profile.ProfileScreen
import com.example.campuspeer.uicomponent.LoginScreen
import com.example.campuspeer.uicomponent.EmailAuthScreen
import com.example.campuspeer.uicomponenti.MainScreen


@Composable
fun NaviGraph(navController: NavHostController,
              startRoute: String,
              currentUserId: String) {
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
                allPosts = dummyPosts,
                selectedCategory = defaultCategory,
                onItemClick = { /* 필요 시 상세 이동 로직 */ }
            )
        }

        composable(Routes.Chat.route) {
            val user = if (currentUserId.isNotEmpty()) currentUserId else "dummyUser"

            // ② onNavigateToChat: roomId와 partnerId를 받아서 ChatRoom 경로로 네비게이트
            val onNavigateToChat: (String, String) -> Unit = { roomId, partnerId ->
                navController.navigate(Routes.ChatRoom.routeWithArgs(roomId, partnerId))
            }
            ChatRoomListScreen(
                currentUserId = user,
                onNavigateToChat = onNavigateToChat
            )
        }

        composable(Routes.Profile.route) {
            ProfileScreen(/* 필요 시 userId 전달 */)
        }

        // 만약 HelpBoard도 BottomNavigation에 포함되어 있다면:
        composable(Routes.HelpBoard.route) {
            HelpBoardScreen(/* 필요 시 인자 전달 */)
        }

        // (선택)각 상세 화면이나 게시글 생성 화면도 필요한 경우 추가
        composable(Routes.PostItemCreate.route) {
            //PostItemCreateScreen()
        }
        // …
    }
}
