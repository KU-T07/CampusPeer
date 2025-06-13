package com.example.campuspeer.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.campuspeer.model.Category
import com.example.campuspeer.model.Message
import com.example.campuspeer.model.PostItem
import java.text.NumberFormat
import java.util.Locale


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

    val isSeller = currentItem?.sellerId == currentUserId

    Column(modifier = Modifier.fillMaxSize()) {
        currentItem?.let { item ->
            if (isSeller) {
                // ── 판매자용 헤더 (상품 + 상태 조절)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ItemTransactionHeader(item = item)
                    Spacer(Modifier.width(8.dp))
                    TransactionStatusDropdown(
                        isSeller = true,
                        currentStatus = status,
                        onStatusChange = { newStatus ->
                            viewModel.updateTransactionStatus(roomId, newStatus)
                        }
                    )
                }
            } else {
                // ── 구매자용 헤더 (상품 정보만)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5))
                        .padding(12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    ItemTransactionHeader(item = item)
                }
            }
        }

        // ── 메시지 리스트
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            reverseLayout = true,
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            itemsIndexed(messages.reversed()) { index, message ->
                val isMe = message.senderId == currentUserId
                val showProfileImage = !isMe &&
                        (index == 0 || messages.reversed()[index - 1].senderId != message.senderId)

                ChatBubble(
                    message = message,
                    isMe = isMe,
                    showProfileImage = showProfileImage
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        // ── 입력창
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
fun ChatRoomLayoutPreviewSimple() {
    // 더미 데이터 준비
    val dummyItem = PostItem(
        id          = "item1",
        title       = "샘플 상품",
        price       = 12345,
        description = "",
        imageUrl    = "",
        category    = Category.ETC,
        status      = "거래가능",
        sellerId    = "userB",
        timestamp   = System.currentTimeMillis(),
        location    = "테스트위치"
    )
    val msg1 = Message(
        senderId  = "userB",
        text      = "안녕하세요!",
        timestamp = System.currentTimeMillis() - 60_000L
    )
    val msg2 = Message(
        senderId  = "userA",
        text      = "네, 반갑습니다!",
        timestamp = System.currentTimeMillis() - 30_000L
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // 1) 상단 헤더(상품 + 상태)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = androidx.compose.ui.graphics.Color(0xFFF5F5F5))
                .padding(12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            ItemTransactionHeader(item = dummyItem)
        }

        // 2) 채팅 메시지 미리보기
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Spacer(modifier = Modifier.height(8.dp))
            ChatBubble(message = msg1, isMe = false, showProfileImage = true)
            Spacer(modifier = Modifier.height(4.dp))
            ChatBubble(message = msg2, isMe = true, showProfileImage = false)
        }

        // 3) 빈 공간 띄우고
        Spacer(modifier = Modifier.weight(1f))

        // 4) 입력창
        MessageInput(onMessageSent = {})
    }
}

