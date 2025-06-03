package com.example.campuspeer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.example.campuspeer.uicomponents.MapComponent.TestScreen
import com.example.campuspeer.auth.LoginScreen
import com.example.campuspeer.navigation.NavGraph
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            var currentUserId by rememberSaveable { mutableStateOf(Firebase.auth.currentUser?.uid) }

            LaunchedEffect(Unit) {
                val uid = Firebase.auth.currentUser?.uid
                Log.d("MainActivity", "초기 로그인 상태 uid = $uid")
                currentUserId = uid
            }

            if (currentUserId == null) {
                LoginScreen(
                    onLoginSuccess = { uid ->
                        Log.d("MainActivity", "로그인 성공, uid = $uid")
                        currentUserId = uid
                    }
                )
                //TestScreen()
            } else {
                Log.d("MainActivity", "NavGraph 진입, uid = $currentUserId")
                NavGraph(navController = navController, currentUserId = currentUserId!!)
            }
        }
    }
}