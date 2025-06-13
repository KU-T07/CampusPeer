package com.example.campuspeer.model

import com.example.campuspeer.R

object NavBarItems{
    val BarItems = listOf(
        BarItem(
            title = "홈",
            selectIcon = R.drawable.baseline_home_24,
            onSelectedIcon = R.drawable.outline_home_24,
            route = Routes.PostItemList.route
        ),
        BarItem(
            title = "게시판",
            selectIcon = R.drawable.baseline_groups_24,
            onSelectedIcon = R.drawable.outline_groups_24,
            route = Routes.HelpBoard.route
        ),
        BarItem(
            title = "채팅",
            selectIcon = R.drawable.baseline_chat_bubble_24,
            onSelectedIcon = R.drawable.outline_chat_bubble_outline_24,
            route = Routes.Chat.route
        ),
        BarItem(
            title = "내정보",
            selectIcon = R.drawable.baseline_person_24,
            onSelectedIcon = R.drawable.outline_person_24,
            route = Routes.Profile.route
        )
    )
}