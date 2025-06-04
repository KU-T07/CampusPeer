package com.example.campuspeer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.campuspeer.model.Routes
import com.example.campuspeer.navigation.mainNavGraph
import com.example.campuspeer.uicomponent.LoginScreen
import com.example.campuspeer.uicomponent.RegisterScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    currentUserId: String
) {

    NavHost(
        navController = navController,
        startDestination = Routes.User.route
    ) {
        navigation(
            startDestination = Routes.Login.route,
            route = Routes.User.route
        ) {
            composable(route = Routes.Login.route) {
                LoginScreen(
                    onLoginSuccess = { navController.navigate(Routes.Home.route) },
                    onRegisterNavigate = { navController.navigate(Routes.Register.route) }
                )
            }


            composable(route = Routes.Register.route) {
                RegisterScreen()
            }
        }
        mainNavGraph(
            navController,
            currentUserId
        )
    }
}

