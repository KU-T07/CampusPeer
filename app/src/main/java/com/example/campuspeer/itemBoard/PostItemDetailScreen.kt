package com.example.campuspeer.itemBoard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.campuspeer.R
import com.example.campuspeer.model.ChatRoom
import com.example.campuspeer.model.PostItem
import com.example.campuspeer.model.Routes
import com.example.campuspeer.model.UserData
import com.example.campuspeer.uicomponents.MapComponent.MapMarkerDisplayScreen
import com.example.campuspeer.util.BackButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

@Composable
fun PostItemDetailScreen(
    navController: NavHostController,
    initialPost: PostItem,
    onBackClick: () -> Unit
) {
    var post by remember { mutableStateOf(initialPost) }
    val currentUserId = Firebase.auth.currentUser?.uid ?: return
    val sellerId = post.sellerId
    val buyerId = currentUserId
    val itemId = post.id
    val roomsRef = Firebase.firestore.collection("chatRooms")

    // 사용자 정보 상태
    var sellerName by remember { mutableStateOf("로딩 중…") }
    var sellerRating by remember { mutableStateOf<Float?>(null) }
    var sellerCount by remember { mutableStateOf<Int?>(null) } // 거래 횟수 상태

    // 스낵바
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // 판매자 정보 불러오기
    LaunchedEffect(sellerId) {
        Firebase.database.reference
            .child("Users")
            .child(sellerId)
            .get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(UserData::class.java)

                sellerName = user?.nickname
                    ?.takeIf { it.isNotBlank() }
                    ?: user?.studentNumber
                        ?.takeIf { it.isNotBlank() }
                            ?: user?.email?.substringBefore("@")
                            ?: "알 수 없음"
                user?.let {
                    val total = it.ratingTotal
                    val count = it.ratingCount
                    sellerRating = if (count > 0) total.toFloat() / count.toFloat() else null
                    sellerCount = if (count > 0) count else null // 거래 횟수 저장
                }
            }

            .addOnFailureListener {
                sellerName = "알 수 없음"
            }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFFFFFF))
        ) {
            BackButton(onClick = onBackClick)

            // 상단 이미지
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = rememberAsyncImagePainter(post.imageUrl),
                    contentDescription = "Item Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(Color(0xFFFFFFFF))
                )
            }

            // 사용자 정보
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF85C687))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF85C687),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(sellerName.take(1)) // 이니셜
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(sellerName, fontWeight = FontWeight.Bold)
                    Text(
                        text = sellerRating?.let { "⭐ ${"%.1f".format(it)} / 5.0" } ?: "평가 없음",
                        style = MaterialTheme.typography.labelSmall
                    )
                    sellerCount?.let {
                        Text("거래 횟수: $it 회", style = MaterialTheme.typography.labelSmall) // ✅ 추가
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 게시글 정보
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(post.title, style = MaterialTheme.typography.titleLarge)
                Text("₩${post.price}", fontWeight = FontWeight.Bold)
                Text(post.category.label)

                Spacer(modifier = Modifier.height(12.dp))

                Text(post.description)
                Spacer(modifier = Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_location_on_24),
                        contentDescription = "Location"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(post.location)
                }
            }

            // 지도
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth()
            ) {
                MapMarkerDisplayScreen(
                    location = post.latlng,
                    locationName = post.location
                )
            }

            // 채팅하기 버튼
            Button(
                onClick = {
                    if (post.status == "거래완료") {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("이미 거래된 상품입니다.")
                        }
                        return@Button
                    }

                    // 채팅방 찾기 or 생성
                    roomsRef
                        .whereEqualTo("itemId", itemId)
                        .whereEqualTo("user1Id", sellerId)
                        .whereEqualTo("user2Id", buyerId)
                        .get()
                        .addOnSuccessListener { snap ->
                            val roomId = if (!snap.isEmpty) {
                                snap.documents[0].id
                            } else {
                                val ref = roomsRef.document()
                                val room = ChatRoom(
                                    id = ref.id,
                                    itemId = itemId,
                                    user1Id = sellerId,
                                    user2Id = buyerId,
                                    participants = listOf(sellerId, buyerId)
                                )
                                ref.set(room)
                                ref.id
                            }

                            navController.navigate(
                                Routes.ChatRoom.create(roomId, sellerId, itemId)
                            )
                        }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81B196)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("채팅하기")
            }
        }
    }
}