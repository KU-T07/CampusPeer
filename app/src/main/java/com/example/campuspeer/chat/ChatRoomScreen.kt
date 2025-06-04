package com.example.campuspeer.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                .padding(horizontal = 8.dp),
            reverseLayout = true,
            contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
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
                    message = message,
                    isMe = isMe,
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

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun ChatRoomScreenPreview() {

    val currentUserId = "T3SDNm5GqYfNSEb8KIqX2aCxFmc2"

    val sampleMessages = remember { mutableStateListOf(
        Message("userB", "안녕하세요! 어떤 물건 찾으세요?", System.currentTimeMillis() - 1000 * 60 * 5, "https://via.placeholder.com/36x36/FF5733/FFFFFF?text=P1"),
        Message("userB", "가격은 조정 가능합니다.", System.currentTimeMillis() - 1000 * 60 * 4, "https://via.placeholder.com/36x36/FF5733/FFFFFF?text=P1"),
        Message("userA", "네, 확인했습니다.", System.currentTimeMillis() - 1000 * 60 * 3, null)
    )}
    var sampleStatus by remember { mutableStateOf("거래 진행") }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            reverseLayout = true,
            contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
        ) {
            itemsIndexed(sampleMessages.reversed()) { index, message ->
                val isMe = message.senderId == currentUserId
                val showProfileImage = when {
                    isMe -> false
                    index == 0 -> true
                    else -> {
                        val previous = sampleMessages.reversed()[index - 1]
                        message.senderId != previous.senderId
                    }
                }
                ChatBubble(
                    message = message,
                    isMe = isMe,
                    showProfileImage = showProfileImage
                )
            }
        }
        TransactionStatusDropdown(
            isSeller = currentUserId == "T3SDNm5GqYfNSEb8KIqX2aCxFmc2",
            currentStatus = sampleStatus,
            onStatusChange = { newStatus ->
                sampleStatus = newStatus
            }
        )
        MessageInput(
            onMessageSent = { text ->
                sampleMessages.add(Message(currentUserId, text, System.currentTimeMillis(), null))
                println("Preview Sent: $text")
            }
        )
    }
}