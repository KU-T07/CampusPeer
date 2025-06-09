package com.example.campuspeer.model
sealed class Routes(val route: String, val isRoot: Boolean = true) {
    object Login : Routes("LoginScreen", false)
    object Register : Routes("RegisterScreen")
    object Main : Routes("MainScreen")
    object PostItemList : Routes("PostItemListScreen")
    object PostItemDetail : Routes("PostItemDetailScreen/{postId}", isRoot = false) {
        fun routeWithId(postId: String): String = "PostItemDetailScreen/$postId"
    }
    object HelpBoard : Routes("HelpBoardScreen")
    object PostItemCreate : Routes("PostItemCreateScreen", isRoot = false)
    object Chat : Routes("chat_list")
    object Profile : Routes("ProfileScreen")

    object ChatRoom : Routes("chat_room/{roomId}/{partnerId}", isRoot = false) {
        fun routeWithArgs(roomId: String, partnerId: String): String {
            return "chat_room/$roomId/$partnerId"
        }
    }


    companion object {
        fun getRoutes(route: String): Routes {
            return when (route) {
                Main.route -> Main
                Register.route -> Register
                HelpBoard.route -> HelpBoard
                PostItemDetail.route -> PostItemDetail
                PostItemCreate.route -> PostItemCreate
                PostItemList.route -> PostItemList
                Chat.route -> Chat
                ChatRoom.route -> ChatRoom
                Profile.route -> Profile
                else -> Main
            }
        }
    }
}
