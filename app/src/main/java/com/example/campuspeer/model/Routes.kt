package com.example.campuspeer.model
sealed class Routes(val route: String, val isRoot: Boolean = true) {
    object Login : Routes("LoginScreen")
    object EmailAuth : Routes("EmailAuthScreen")
    object Home : Routes("Home")
    object ItemBoard : Routes("ItemBoard")
    object ItemInfo : Routes("ItemInfo")

    object AddItem : Routes("AddItem", isRoot = false)
    object Chat : Routes("chat_list")

    object ChatRoom : Routes("chat_room/{roomId}/{partnerId}", isRoot = false) {
        fun routeWithArgs(roomId: String, partnerId: String): String {
            return "chat_room/$roomId/$partnerId"
        }
    }

    object Profile : Routes("Profile")
    object User : Routes("User")
    object Login : Routes("Login", false)
    object Register : Routes("Register",false)
    object ItemBoard : Routes("ItemBoard", false)

    companion object {
        fun getRoutes(route: String): Routes {
            return when (route) {
                Home.route -> Home
                HelpBoard.route -> HelpBoard
                ItemInfo.route -> ItemInfo
                AddItem.route -> AddItem
                Chat.route -> Chat
                ChatRoom.route -> ChatRoom
                Profile.route -> Profile
                else -> Home
            }
        }
    }
}
