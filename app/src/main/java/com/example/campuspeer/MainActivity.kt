package com.example.campuspeer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import com.example.campuspeer.model.Routes
import com.example.campuspeer.navigation.NaviGraph
import com.example.campuspeer.uicomponent.LoginScreen
import com.example.campuspeer.viewmodel.EmailAuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.example.campuspeer.ui.theme.CampusPeerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val emailAuthVm: EmailAuthViewModel by viewModels()
        setContent {
            CampusPeerTheme(
                dynamicColor = false // ← 중요!
            ) {
                CompositionLocalProvider(
                    LocalEmailAuthViewModelOwner provides this
                ) {
                    val navController = rememberNavController()
                    NaviGraph(navController = navController, Routes.Login.route, "")
                }
            }
        }
    }
}
val LocalEmailAuthViewModelOwner = staticCompositionLocalOf<ViewModelStoreOwner> {
    error("EmailAuthViewModelOwner가 CompositionLocal에 제공되지 않았습니다.")
}