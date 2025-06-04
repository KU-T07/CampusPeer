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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campuspeer.model.Message

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

    val isSeller = currentUserId == "T3SDNm5GqYfNSEb8KIqX2aCxFmc2"

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


@Preview(showBackground = true)
@Composable
fun ChatBubbleListPreview() {
    val currentUserId = "userA"
    val sampleMessages = listOf(
        Message("userB", "안녕하세요!", System.currentTimeMillis(), "https://example.com/profileB.png"),
        Message(
            "userB",
            "연속으로 보냅니다.",
            System.currentTimeMillis(),
            "https://example.com/profileB.png"
        ),
        Message("userA", "반갑습니다!", System.currentTimeMillis(), "https://example.com/profileA.png")
    )

    Column(modifier = Modifier.padding(8.dp)) {
        sampleMessages.forEachIndexed { index, message ->
            val isMe = message.senderId == currentUserId
            val showProfileImage = !isMe && (
                    index == 0 || sampleMessages[index - 1].senderId != message.senderId
                    )

            ChatBubble(
                text = message.text,
                isMe = isMe,
                profileImageUrl = message.profileImageUrl,
                showProfileImage = showProfileImage
            )
        }
    }
}