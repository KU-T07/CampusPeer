package com.example.campuspeer.model

sealed class Routes (val route: String, val isRoot: Boolean = true) {
    object Main : Routes("Main")
    object Home : Routes("Home")
    object PostItemListScreen : Routes("PostItemListScreen")
    object HelpBoard : Routes("HelpBoard")
    object ItemInfo : Routes("ItemINfo")
    object AddItem : Routes("AddItem", isRoot = false)
    object Chat : Routes("Chat")
    object Profile : Routes("Profile")
    object User : Routes("User")
    object Login : Routes("Login", false)
    object Register : Routes("Register",false)

    companion object{
        fun getRoutes(route:String): Routes{
            return when(route){
                Home.route -> Home
                HelpBoard.route -> HelpBoard
                ItemInfo.route -> ItemInfo
                AddItem.route -> AddItem
                Chat.route -> Chat
                Profile.route -> Profile
                else -> Home
            }
        }
    }
}