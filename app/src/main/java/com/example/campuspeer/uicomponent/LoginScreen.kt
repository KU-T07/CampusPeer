package com.example.campuspeer.uicomponent

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campuspeer.LocalEmailAuthViewModelOwner
import com.example.campuspeer.ui.theme.Pretendard
import com.example.campuspeer.viewmodel.EmailAuthViewModel

@Composable
fun LoginScreen(
    onNavigateToMain: (String) -> Unit,
    onRegisterNavigate: () -> Unit,
) {
    val context = LocalContext.current

    val owner: ViewModelStoreOwner = LocalEmailAuthViewModelOwner.current
    val viewModel: EmailAuthViewModel = viewModel(viewModelStoreOwner = owner)


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("CampusPeer",  style = TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 52.sp
        ))

        Spacer(modifier = Modifier.height(3.dp))
        Text("신뢰 할 수 있는 교내 거래 플랫폼",  style = TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        ))

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = {
            Text("Email", style = TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp)) })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", style = TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp)) },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(22.dp))

        Button(

            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "이메일과 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                viewModel.login(email, password) { success, error ->
                    if (success) {
                        print("성공")
                        // 로그인 성공 시 화면 이동
                        onNavigateToMain(viewModel.getCurrentUserId() ?: "") // userId가 필요 없다면 이대로
                    } else {
                        Toast.makeText(context, error ?: "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.width(300.dp)
        ) {
            Text("Log In", style = TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            onRegisterNavigate()
        }) {
            Text("계정이 없으신가요? Sign Up", style = TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp))
        }
    }
}
