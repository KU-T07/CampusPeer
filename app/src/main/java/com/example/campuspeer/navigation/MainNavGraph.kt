package com.example.campuspeer.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.campuspeer.chat.ChatRoomListScreen
import com.example.campuspeer.chat.ChatRoomScreen
import com.example.campuspeer.helpBoard.HelpBoardScreen
import com.example.campuspeer.itemBoard.PostItemCreateScreen
import com.example.campuspeer.itemBoard.PostItemListScreen
import com.example.campuspeer.model.Category
import com.example.campuspeer.model.Routes

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    currentUserId: String
) {
    navigation(
        startDestination = Routes.Main.route,
        route = Routes.Main.route
    ) {
        composable(Routes.Main.route) {
            PostItemListScreen(
                allPosts = emptyList(), // 또는 ViewModel에서 받아온 데이터
                selectedCategory = Category.ETC,
                navController = navController // 🔥 이거 꼭 추가!
            )
        }
        composable(Routes.HelpBoard.route) {
            HelpBoardScreen()
        }


        composable(Routes.PostItemCreate.route) {
            PostItemCreateScreen(
                navController = navController,  // 전달
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("chat_list") {
            ChatRoomListScreen(
                currentUserId = currentUserId,
                onNavigateToChat = { roomId, partnerId, itemId ->
                    navController.navigate(Routes.ChatRoom.create(roomId, partnerId, itemId))
                }
            )
        }

        composable("chat_room/{roomId}/{partnerId}") { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") ?: return@composable
            val partnerId = backStackEntry.arguments?.getString("partnerId") ?: return@composable

            ChatRoomScreen(
                roomId = roomId,
                currentUserId = currentUserId,
                partnerId = partnerId,
                itemId = TODO(),
                viewModel = TODO()
            )
        }
    }
}
