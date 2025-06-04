package com.example.campuspeer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.campuspeer.model.Routes
import com.example.campuspeer.navigation.NaviGraph
import com.example.campuspeer.uicomponent.LoginScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NaviGraph(navController = navController, Routes.Login.route,"T3SDNm5GqYfNSEb8KIqX2aCxFmc2")
        }
    }
}