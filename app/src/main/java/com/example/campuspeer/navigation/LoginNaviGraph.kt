package com.example.campuspeer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.campuspeer.chat.ChatRoomListScreen
import com.example.campuspeer.chat.ChatRoomScreen
import com.example.campuspeer.model.Routes
import com.example.campuspeer.uicomponent.LoginScreen
import com.example.campuspeer.uicomponent.EmailAuthScreen
import com.example.campuspeer.uicomponent.WelcomeScreen


@Composable
fun LoginNaviGraph(navController: NavHostController,
                   currentUserId: String) {
    NavHost(navController = navController, startDestination = Routes.Login.route) {

        composable(Routes.Login.route) {
            LoginScreen(
                onNavigateToWelcome = { userId ->
                    navController.navigate(Routes.Home.route + "/$userId")
                },
                onRegisterNavigate = {
                    navController.navigate(Routes.EmailAuth.route)
                }
            )
        }
        composable(Routes.ItemBoard.route) {
            //ItemBoardScreen()
        }

        composable(Routes.ItemInfo.route + "/{itemId}", arguments = listOf(
            navArgument("itemId") { type = NavType.StringType }
        )) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            //ItemInfoScreen(itemId = itemId)
        }

        composable(Routes.AddItem.route) {
          //  AddItemScreen()
        }


        composable(Routes.Profile.route) {
           // ProfileScreen()
        }

        composable(Routes.Chat.route) {
            ChatRoomListScreen(
                currentUserId = currentUserId,
                onNavigateToChat = { roomId, partnerId ->
                    navController.navigate(Routes.ChatRoom.routeWithArgs(roomId, partnerId))
                }
            )
        }

        composable(
            route = Routes.ChatRoom.route,
            arguments = listOf(
                navArgument("roomId") { type = NavType.StringType },
                navArgument("partnerId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") ?: return@composable
            val partnerId = backStackEntry.arguments?.getString("partnerId") ?: return@composable

            ChatRoomScreen(
                roomId = roomId,
                currentUserId = currentUserId,
                partnerId = partnerId
            )
        }


        composable(Routes.EmailAuth.route) {
            EmailAuthScreen(
                onNavigateToLogin = {
                    navController.popBackStack() // 뒤로가기
                }
            )
        }
        composable(
            route = Routes.Home.route + "/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            WelcomeScreen()
        }

    }
}
