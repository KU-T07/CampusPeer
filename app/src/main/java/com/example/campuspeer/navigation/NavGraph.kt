package com.example.campuspeer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.campuspeer.chat.ChatRoomListScreen
import com.example.campuspeer.chat.ChatRoomScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    currentUserId: String
) {
    NavHost(
        navController = navController,
        startDestination = "chat_list"
    ){
        composable("chat_list"){
            ChatRoomListScreen(
                currentUserId = currentUserId,
                onNavigateToChat = {roomId, partnerId ->
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