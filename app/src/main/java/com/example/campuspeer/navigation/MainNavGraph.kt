package com.example.campuspeer.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
                allUsers = emptyList(),
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

        composable(
            route = "chat_room/{roomId}/{partnerId}/{itemId}",
            arguments = listOf(
                navArgument("roomId")    { type = NavType.StringType },
                navArgument("partnerId") { type = NavType.StringType },
                navArgument("itemId")    { type = NavType.StringType }
            )
        ) { backStack ->
            val roomId    = backStack.arguments!!.getString("roomId")!!
            val partnerId = backStack.arguments!!.getString("partnerId")!!
            val itemId    = backStack.arguments!!.getString("itemId")!!

            ChatRoomScreen(
                roomId        = roomId,
                currentUserId = currentUserId,
                partnerId     = partnerId,
                itemId        = itemId
            )
        }

    }
}