package com.example.campuspeer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.campuspeer.itemBoard.PostItemCreateScreen
import com.example.campuspeer.ui.theme.CampusPeerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CampusPeerTheme {
                val navController = rememberNavController()

                PostItemCreateScreen(
                    navController = navController,
                    onBackClick = {})
            }
        }
    }
}