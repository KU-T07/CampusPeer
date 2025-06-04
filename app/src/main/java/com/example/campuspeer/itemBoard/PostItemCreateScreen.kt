package com.example.campuspeer.itemBoard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.campuspeer.model.PostItem
import com.example.campuspeer.util.BackButton

@Composable
fun PostItemCreateScreen(
    onBackClick: () -> Unit,
    onPostSubmit: (PostItem) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("거래가능") }
    var imageUrl by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            BackButton(onClick = onBackClick)
            Spacer(modifier = Modifier.width(8.dp))
            Text("게시글 등록", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("제목") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("가격") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("설명") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("카테고리") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 상태 선택
        Text("거래 상태", style = TextStyle(fontSize = 14.sp))
        Row {
            listOf("거래가능", "예약중", "거래완료").forEach {
                val selected = it == status
                Text(
                    text = it,
                    color = if (selected) Color.Blue else Color.Gray,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clickable { status = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 이미지 업로드
        Text("이미지 (URL)", style = TextStyle(fontSize = 14.sp))
        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            placeholder = { Text("https://...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (imageUrl.isNotEmpty()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Uploaded Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color.LightGray)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 등록 버튼
        Button(
            onClick = {
                val post = PostItem(
                    title = title,
                    price = price,
                    description = description,
                    category = category,
                    imageUrl = imageUrl,
                    status = status,
                    timestamp = System.currentTimeMillis(),
                    sellerId = "dummyUserId", // 실제 사용자 ID로 교체
                    location = "건국대학교"
                )
                onPostSubmit(post)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("등록")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPostItemCreateScreen() {
    PostItemCreateScreen(
        onBackClick = TODO(),
        onPostSubmit = TODO()
    )
}
