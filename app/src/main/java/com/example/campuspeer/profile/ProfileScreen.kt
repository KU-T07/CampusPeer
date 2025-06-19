package com.example.campuspeer.profile

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campuspeer.LocalEmailAuthViewModelOwner
import com.example.campuspeer.viewmodel.EmailAuthViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
@Composable
fun ProfileScreen() {
    val owner = LocalEmailAuthViewModelOwner.current
    val viewModel: EmailAuthViewModel = viewModel(viewModelStoreOwner = owner)

    var nickname by remember { mutableStateOf("로딩 중...") }
    var department by remember { mutableStateOf("로딩 중...") }
    var email by remember { mutableStateOf("로딩 중...") }
    var studentNumber by remember { mutableStateOf("로딩 중...") }
    var rating by remember { mutableStateOf(0.0f) }
    var count by remember { mutableStateOf(0) }

    val usersRef = Firebase.database.getReference("Users")
    LaunchedEffect(Unit) {
        usersRef.child(viewModel.getCurrentUserId().toString()).get()
            .addOnSuccessListener { snapshot ->
                nickname = snapshot.child("nickname").getValue(String::class.java) ?: ""
                department = snapshot.child("department").getValue(String::class.java) ?: ""
                email = snapshot.child("email").getValue(String::class.java) ?: ""
                studentNumber = snapshot.child("studentNumber").getValue(String::class.java) ?: ""

                val ratingTotal = snapshot.child("ratingTotal").getValue(Double::class.java) ?: 0.0
                val ratingCount = snapshot.child("ratingCount").getValue(Long::class.java) ?: 0L
                rating = if (ratingCount > 0) (ratingTotal / ratingCount).toFloat() else 0.0f
                count = snapshot.child("ratingCount").getValue(Int::class.java) ?: 0
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F4FC))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Account",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                ProfileItem(Icons.Default.Person, "닉네임", nickname)
                Spacer(modifier = Modifier.height(12.dp))
                ProfileItem(Icons.Default.School, "학과", department)
                Spacer(modifier = Modifier.height(12.dp))
                ProfileItem(Icons.Default.Star, "평점", String.format("%.1f / 5", rating), Color(0xFFFFD700))
                Spacer(modifier = Modifier.height(12.dp))
                ProfileItem(Icons.Default.Person, "거래횟수", count.toString())
                Spacer(modifier = Modifier.height(12.dp))
                ProfileItem(Icons.Default.Person, "이메일", email)
                Spacer(modifier = Modifier.height(12.dp))
                ProfileItem(Icons.Default.Person, "학번", studentNumber)
            }
        }
    }
}
@Composable
fun ProfileItem(icon: ImageVector, label: String, value: String, iconTint: Color = Color.Black) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = label, tint = iconTint, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "$label: ",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
        Text(
            text = value,
            fontSize = 16.sp
        )
    }
}
