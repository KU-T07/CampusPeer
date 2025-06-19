package com.example.campuspeer.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.campuspeer.model.PostItem
import com.example.campuspeer.model.UserData
import com.example.campuspeer.uicomponent.RatingDialog
import com.example.campuspeer.util.RatingUtils
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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
    // 1) StateFlows 구독
    val messages   by viewModel.messages.collectAsState()
    val status     by viewModel.status.collectAsState()
    val currentItem by viewModel.currentItem.collectAsState()
    var partnerName by remember { mutableStateOf<String?>("로딩 중…") }

    val sellerId = currentItem?.sellerId ?: ""
    val isSeller = sellerId.trim() == currentUserId.trim()

    // 평점 기능 변수
    var showRatingDialog by remember { mutableStateOf(false) }
    var alreadyRated by remember { mutableStateOf(false) }

    println("🪪 currentUserId = '$currentUserId'")
    println("📦 sellerId from item = '$sellerId'")
    println("🔍 isSeller = $isSeller")

    // 거래 상태가 완료되면 다이얼로그 표시
    LaunchedEffect(status) {
        if (status == "거래완료" && !alreadyRated) {
            showRatingDialog = true
        }
    }

    // 3) 메시지·상태 리스너
    LaunchedEffect(roomId) {
        viewModel.listenForMessage(roomId)
        viewModel.listenForTransactionStatus(roomId)

        // 평점 매기기 완료했는지 확인
        val ref = Firebase.database
            .getReference("RatingsDone")
            .child(roomId)
            .child(currentUserId)

        ref.get().addOnSuccessListener { snapshot ->
            val done = snapshot.getValue(Boolean::class.java) ?: false
            alreadyRated = done
        }
    }
    // 4) 상품 정보 로드
    LaunchedEffect(itemId) {
        println("🧐 fetchItemDetails 호출, itemId=$itemId")
        viewModel.fetchItemSummaryForChat(itemId)
    }
    // 3) 파트너 정보 로드 (Realtime DB → Users/{uid})
    LaunchedEffect(partnerId) {
        Firebase
            .database
            .reference
            .child("Users")
            .child(partnerId)
            .get()
            .addOnSuccessListener { snapshot ->
                snapshot.getValue(UserData::class.java)?.let { user ->
                    // 닉네임이 비어있으면 studentNumber 또는 email 앞부분으로 대체
                    partnerName = user.nickname
                        .takeIf { it.isNotBlank() }
                        ?: user.studentNumber
                            .takeIf { it.isNotBlank() }
                                ?: user.email
                            .substringBefore("@")
                } ?: run {
                    partnerName = "알 수 없는 사용자"
                }
            }
            .addOnFailureListener {
                partnerName = "알 수 없는 사용자"
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // ─── 채팅방 최상단: 상대방 이름
        Text(
            text = partnerName ?: "로딩 중…",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        // ─── 상품 정보 헤더 (판매자면 상태 드롭다운까지)
        currentItem?.let { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ItemTransactionHeader(
                    item = item,
                    isSeller = isSeller,
                    status = status,
                    onStatusChange = { newStatus ->
                        viewModel.updateTransactionStatus(roomId, newStatus)
                    }
                )
            }
        }


        // ─── 메시지 리스트
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            reverseLayout = true,
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            itemsIndexed(messages.reversed()) { index, message ->
                val isMe = message.senderId == currentUserId
                val showAvatar = !isMe &&
                        (index == 0 || messages.reversed()[index - 1].senderId != message.senderId)

                ChatBubble(
                    message = message,
                    isMe = isMe,
                    showProfileImage = showAvatar
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        // ─── 메시지 입력창
        MessageInput { text ->
            viewModel.sendMessage(roomId, currentUserId, text)
        }
    }

    if (showRatingDialog) {
        RatingDialog(
            targetUserId = partnerId,
            onSubmit = { rating ->
                RatingUtils.updateUserRating(partnerId, rating) { success ->
                    if (success) {
                        RatingUtils.markRatingDone(roomId, currentUserId)
                        showRatingDialog = false
                        alreadyRated = true
                    }
                }
            },
            onDismiss = {
                showRatingDialog = false
            }
        )
    }
}

@Composable
fun ItemTransactionHeader(
    item: PostItem,
    isSeller: Boolean,
    status: String,
    onStatusChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 왼쪽: 이미지 + 텍스트
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = "상품 이미지",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(item.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(
                    "${NumberFormat.getNumberInstance(Locale.getDefault()).format(item.price)}원",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(Modifier.width(16.dp))

        // 오른쪽: 거래 상태
        if (isSeller) {
            TransactionStatusDropdown(
                isSeller = true,
                currentStatus = status,
                onStatusChange = onStatusChange
            )
        } else {
            val statusColor = when (status) {
                "거래가능" -> Color(0xFF4CAF50)
                "예약중" -> Color(0xFFFF9800)
                "거래완료" -> Color(0xFFF44336)
                else -> Color.Gray
            }

            Text(
                text = status,
                fontSize = 14.sp,
                color = statusColor,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF0F0F0))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

