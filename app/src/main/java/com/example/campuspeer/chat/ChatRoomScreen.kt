package com.example.campuspeer.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ChatRoomScreen(
    roomId: String,
    currentUserId: String,
    partnerId: String,
    viewModel: ChatViewModel = viewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val status by viewModel.status.collectAsState()

    LaunchedEffect(roomId) {
        viewModel.listenForTransactionStatus(roomId)
        viewModel.listenForMessage(roomId)
    }

    val isSeller = currentUserId == "userA"

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            reverseLayout = true
        ) {
            itemsIndexed(messages.reversed()) { index, message ->
                val isMe = message.senderId == currentUserId
                val showProfileImage = when {
                    isMe -> false
                    index == 0 -> true
                    else -> {
                        val previous = messages.reversed()[index - 1]
                        message.senderId != previous.senderId
                    }
                }

                ChatBubble(
                    text = message.text,
                    isMe = isMe,
                    profileImageUrl = message.profileImageUrl,
                    showProfileImage = showProfileImage
                )
            }
        }
        TransactionStatusDropdown(
            isSeller = isSeller,
            currentStatus = status,
            onStatusChange = { newStatus ->
                viewModel.updateTransactionStatus(roomId, newStatus)
            }
        )


        MessageInput(
            onMessageSent = { text ->
                viewModel.sendMessage(roomId, currentUserId, text)
            }
        )
    }
}