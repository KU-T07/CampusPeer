package com.example.campuspeer.uicomponent

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RatingDialog(
    targetUserId: String,                      // 평가 대상 UID
    onSubmit: (Int) -> Unit,                   // 별점 제출 콜백
    onDismiss: () -> Unit                      // 취소 콜백
) {
    var selectedRating by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onSubmit(selectedRating) }) {
                Text("등록")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        },
        title = { Text("평점 남기기") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("상대방에게 평점을 주세요")
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (i in 1..5) {
                        IconToggleButton(
                            checked = i <= selectedRating,
                            onCheckedChange = { selectedRating = i }
                        ) {
                            Icon(
                                imageVector = if (i <= selectedRating) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFC107)
                            )
                        }
                    }
                }
            }
        }
    )
}
