package com.example.campuspeer.model
sealed class Routes(val route: String, val isRoot: Boolean = true) {
    object Login : Routes("로그인", false)
    object Register : Routes("RegisterScreen", false)
    object Main : Routes("MainScreen")
    object PostItemList : Routes("게시글")
    object PostItemDetail : Routes("PostItemDetailScreen/{postId}", isRoot = false) {
        fun routeWithId(postId: String): String = "PostItemDetailScreen/$postId"
    }
    object HelpBoard : Routes("HelpBoardScreen")
    object PostItemCreate : Routes("PostItemCreateScreen", isRoot = false)
    object Chat : Routes("채팅")
    object Profile : Routes("내정보")

    object ChatRoom :
        Routes("chat_room/{roomId}/{partnerId}/{itemId}", isRoot = false) {
        fun create(roomId: String, partnerId: String, itemId: String) =
            "chat_room/$roomId/$partnerId/$itemId"
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
