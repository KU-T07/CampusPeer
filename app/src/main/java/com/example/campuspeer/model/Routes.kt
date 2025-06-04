package com.example.campuspeer.model

sealed class Routes (val route: String, val isRoot: Boolean = true) {
    object Login : Routes("LoginScreen")
    object EmailAuth : Routes("EmailAuthScreen")
    object Home : Routes("Home")
    object ItemBoard : Routes("ItemBoard")
    object ItemInfo : Routes("ItemINfo")
    object AddItem : Routes("AddItem", isRoot = false)
    object Chat : Routes("Chat")
    object Profile : Routes("Profile")

    companion object{
        fun getRoutes(route:String): Routes{
            return when(route){
                Home.route -> Home
                ItemBoard.route -> ItemBoard
                ItemInfo.route -> ItemInfo
                AddItem.route -> AddItem
                Chat.route -> Chat
                Profile.route -> Profile
                else -> Home
            }
        }
    }
}