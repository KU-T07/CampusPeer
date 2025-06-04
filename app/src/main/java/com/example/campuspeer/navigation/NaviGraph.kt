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
import com.example.campuspeer.uicomponent.MainScreen


@Composable
fun NaviGraph(navController: NavHostController,
                   currentUserId: String) {
    NavHost(navController = navController, startDestination = Routes.Login.route) {

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
        }
    }
}
