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
import com.example.campuspeer.model.Routes

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    currentUserId: String
) {
    navigation(
        startDestination = Routes.Home.route,
        route = Routes.Main.route
    ) {
        composable(Routes.Home.route) {
            PostItemListScreen(
                allPosts = TODO(),
                selectedCategory = TODO()
            ) { }
        }
        composable(Routes.HelpBoard.route) {
            HelpBoardScreen()
        }


        composable(Routes.AddItem.route) {
            PostItemCreateScreen(
                navController = navController,  // 전달
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("chat_list") {
            ChatRoomListScreen(
                currentUserId = currentUserId,
                onNavigateToChat = { roomId, partnerId ->
                    navController.navigate("chat_room/$roomId/$partnerId")
                }
            )
        }
        composable("chat_room/{roomId}/{partnerId}") { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") ?: return@composable
            val partnerId = backStackEntry.arguments?.getString("partnerId") ?: return@composable

            ChatRoomScreen(
                roomId = roomId,
                currentUserId = currentUserId,
                partnerId = partnerId
            )
        }
    }
}