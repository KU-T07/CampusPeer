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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.campuspeer.R
import com.example.campuspeer.model.PostItem
import com.example.campuspeer.util.BackButton

@Composable
fun PostItemDetailScreen(
    post: PostItem,
    onBackClick: () -> Unit,
    onChatClick: () -> Unit
) {
    val navController = rememberNavController()

    /*Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "contacts",
            tint = Color.Blue,
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.Center)
        )
    }*/

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

            BackButton(
                onClick = onBackClick,
                modifier = Modifier.padding(8.dp)
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

        Spacer(modifier = Modifier.weight(1f))

        // 채팅하기 버튼
        Button(
            onClick = onChatClick,
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

@Preview(showBackground = true)
@Composable
fun PostItemDetailScreenPreview() {
    val dummyPost = PostItem(
        id = "1",
        title = "애플펜슬 2세대",
        price = 85000,
        description = "거의 새상품 수준으로 깨끗합니다. 애플펜슬 2세대입니다. 직거래만 가능해요!",
        imageUrl = "https://via.placeholder.com/300",
        category = com.example.campuspeer.model.Category.ELECTRONICS,
        status = "예약중",
        sellerId = "dummyUserId",
        timestamp = System.currentTimeMillis(),
        location = "건국대학교"
    )

    PostItemDetailScreen(
        post = dummyPost,
        onBackClick = {},
        onChatClick = {}
    )
}