package com.example.campuspeer.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campuspeer.LocalEmailAuthViewModelOwner
import com.example.campuspeer.viewmodel.EmailAuthViewModel

@Composable
fun ProfileScreen(
    nickname: String = "홍길동",
    department: String = "컴퓨터공학과",
    rating: Float = 4.5f
) {

    val owner: ViewModelStoreOwner = LocalEmailAuthViewModelOwner.current
    val viewModel: EmailAuthViewModel = viewModel(viewModelStoreOwner = owner)


    val nickname = viewModel.getCurrentUserId().toString()
    val department = viewModel.getCurrentUserId().toString()
    val rating = 4.5f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Account", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Person, contentDescription = "닉네임")
            Spacer(Modifier.width(8.dp))
            Text(text = nickname)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.School, contentDescription = "학과")
            Spacer(Modifier.width(8.dp))
            Text(text = department)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = "평점", tint = Color(0xFFFFD700))
            Spacer(Modifier.width(8.dp))
            Text(String.format("%.1f / 4.5", rating))
        }
    }
}
