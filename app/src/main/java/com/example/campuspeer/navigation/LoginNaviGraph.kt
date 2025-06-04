package com.example.campuspeer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.campuspeer.model.Routes
import com.example.campuspeer.uicomponent.LoginScreen
import com.example.campuspeer.uicomponent.EmailAuthScreen
import com.example.campuspeer.uicomponent.WelcomeScreen


@Composable
fun LoginNaviGraph(navController: NavHostController) {
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

        composable(Routes.EmailAuth.route) {
            EmailAuthScreen(
                onNavigateToLogin = {
                    navController.popBackStack() // 뒤로가기
                }
            )
        }
        composable(
            route = Routes.Home.route + "/{userId}",   // ✅ 수정
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            WelcomeScreen()
        }

    }
}
