package com.example.campuspeer.uicomponent

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.campuspeer.util.RatingUtils

@Composable
fun RatingDialog(
    targetUserId: String,
    onSubmit: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    // 별점 상태
    var selectedRating by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (selectedRating > 0) {
                        onSubmit(selectedRating)
                    }
                }
            ) {
                Text("등록")
            }
        },

        title = { Text("평점 남기기") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("상대방에게 평점을 주세요", modifier = Modifier.padding(bottom = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (i in 1..5) {
                        IconButton(
                            onClick = {
                                selectedRating = if (selectedRating == i) 0 else i
                            }
                        ) {
                            Icon(
                                imageVector = if (i <= selectedRating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                contentDescription = "${i}점",
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}
