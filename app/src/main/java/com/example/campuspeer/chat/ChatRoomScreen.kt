package com.example.campuspeer.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.campuspeer.model.Message
import com.example.campuspeer.model.PostItem
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.text.NumberFormat
import java.util.*


@Composable
fun ChatRoomScreen(
    roomId: String,
    currentUserId: String,
    partnerId: String,
    itemId: String,
    viewModel: ChatViewModel = viewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val status by viewModel.status.collectAsState()
    val currentItem by viewModel.currentItem.collectAsState()

    LaunchedEffect(roomId) {
        viewModel.listenForTransactionStatus(roomId)
        viewModel.listenForMessage(roomId)
    }

    LaunchedEffect(itemId) {
        viewModel.fetchItemDetails(itemId)
    }

    val isSeller = currentUserId == "T3SDNm5GqYfNSEb8KIqX2aCxFmc2"

    Column(modifier = Modifier.fillMaxSize()) {
        currentItem?.let { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ItemTransactionHeader(item = item)
                Spacer(modifier = Modifier.width(8.dp))
                TransactionStatusDropdown(
                    isSeller = isSeller,
                    currentStatus = status,
                    onStatusChange = { newStatus ->
                        viewModel.updateTransactionStatus(roomId, newStatus)
                    }
                )
            }
        }

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

        MessageInput(
            onMessageSent = { text ->
                viewModel.sendMessage(roomId, currentUserId, text)
            }
        )
    }

}

@Composable
fun ItemTransactionHeader(item: PostItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = "물건 이미지",
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${NumberFormat.getNumberInstance(Locale.getDefault()).format(item.price)}원",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun ChatRoomScreenPreview() {
    val currentUserId = "T3SDNm5GqYfNSEb8KIqX2aCxFmc2"
    val sampleItemId = "sampleItemId123"

    val sampleItem = PostItem(
        id = sampleItemId,
        title = "새내기 필수템",
        price = 1000,
        imageUrl = "https://via.placeholder.com/150"
    )

    val sampleMessages = remember {
        mutableStateListOf(
            Message(
                senderId = "userB",
                text = "안녕하세요!",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 5,
                profileImageUrl = "https://via.placeholder.com/36x36/FF5733/FFFFFF?text=P1"
            ),
            Message(
                senderId = "userB",
                text = "가격은 조정 가능합니다.",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 4,
                profileImageUrl = "https://via.placeholder.com/36x36/FF5733/FFFFFF?text=P1"
            ),
            Message(
                senderId = currentUserId,
                text = "네, 괜찮습니다.",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 3,
                profileImageUrl = null
            )
        )
    }

    var sampleStatus by remember { mutableStateOf("거래 진행") }

    Column(modifier = Modifier.fillMaxSize()) {
        // 한 줄에 배치된 Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ItemTransactionHeader(item = sampleItem)
            Spacer(modifier = Modifier.width(8.dp))
            TransactionStatusDropdown(
                isSeller = true,
                currentStatus = sampleStatus,
                onStatusChange = { sampleStatus = it }
            )
        }

        // 채팅 메시지
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

        // 메시지 입력창
        MessageInput(
            onMessageSent = { text ->
                sampleMessages.add(
                    Message(currentUserId, text, System.currentTimeMillis())
                )
            }
        )
    }
}
