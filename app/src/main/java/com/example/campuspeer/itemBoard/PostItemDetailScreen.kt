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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.campuspeer.uicomponents.MapComponent.MapMarkerDisplayScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore


@Composable
fun PostItemDetailScreen(
    navController: NavHostController,
    initialPost: PostItem,
    onBackClick: () -> Unit
) {
    //val navController = rememberNavController()
    var post by remember { mutableStateOf(initialPost) }
    val currentUserId = Firebase.auth.currentUser?.uid ?: return
    val sellerId      = post.sellerId
    val buyerId       = currentUserId
    val itemId        = post.id
    val roomsRef      = Firebase.firestore.collection("chatRooms")


    Column(modifier = Modifier.fillMaxSize()) {
        // 상단 이미지 & 뒤로가기
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = rememberAsyncImagePainter(post.imageUrl),
                contentDescription = "Item Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(Color(0xFFF0EEF5))
            )

        }

        // 사용자 정보
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8F0FF))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFFE5D8FF),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("A") // 임시 사용자 이니셜
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text("이름", fontWeight = FontWeight.Bold)
                Text("평점", style = MaterialTheme.typography.labelSmall)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 제목, 가격, 카테고리, 설명
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

        Box(modifier = Modifier
            .weight(2f)
            .fillMaxWidth()) {
            MapMarkerDisplayScreen(
                location = post.latlng,
                locationName = post.location
            )
        }
        // 채팅하기 버튼
        Button(
            onClick = {
                // 1) 같은 (itemId, sellerId, buyerId) 방 검색
                roomsRef
                    .whereEqualTo("itemId", itemId)
                    .whereEqualTo("user1Id", sellerId)
                    .whereEqualTo("user2Id", buyerId)
                    .get()
                    .addOnSuccessListener { snap ->
                        val roomId = if (!snap.isEmpty) {
                            // 기존 방
                            snap.documents[0].id
                        } else {
                            // 새 방 생성
                            val ref = roomsRef.document()
                            val room = ChatRoom(
                                id           = ref.id,
                                itemId       = itemId,
                                user1Id      = sellerId,
                                user2Id      = buyerId,
                                participants = listOf(sellerId, buyerId)
                            )
                            ref.set(room)
                            ref.id
                        }
                        // 2) roomId, partnerId(seller), itemId 넘겨서 이동
                        navController.navigate(Routes.ChatRoom.create(roomId, sellerId, itemId))
                    }
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9C9FF)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("채팅하기")
        }
    }
}
