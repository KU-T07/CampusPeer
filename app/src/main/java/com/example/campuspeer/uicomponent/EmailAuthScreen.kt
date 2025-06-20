package com.example.campuspeer.uicomponent

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campuspeer.LocalEmailAuthViewModelOwner
import com.example.campuspeer.ui.theme.Pretendard
import com.example.campuspeer.util.sendEmailToAdmin
import com.example.campuspeer.viewmodel.EmailAuthViewModel

@Composable
fun EmailAuthScreen(
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current

    val owner: ViewModelStoreOwner = LocalEmailAuthViewModelOwner.current
    val viewModel: EmailAuthViewModel = viewModel(viewModelStoreOwner = owner)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") } //별명 추가
    var department by remember { mutableStateOf("") }
    var studentNumber by remember { mutableStateOf("") }
    var studentIdUri by remember { mutableStateOf<Uri?>(null) }
    var emailVerified by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            studentIdUri = result.data?.data
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("CampusPeer", style = TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp))
        Text("Sign Up", style = TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp))

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))



        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                if (email.isBlank()) {
                    Toast.makeText(context, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                viewModel.sendVerificationEmail(email) { success, error ->
                    message = if (success) {
                        "인증 메일이 발송되었습니다. 메일을 확인해주세요."
                    } else {
                        "메일 발송 실패: $error"
                    }
                }
            }) {
                Text("인증 메일 보내기")
            }

            Button(onClick = {
                viewModel.checkEmailVerified {
                    emailVerified = it
                    message = if (it) "이메일 인증 완료됨!" else "아직 인증되지 않았습니다."
                }
            }) {
                Text("이메일 인증 확인")
            }
        }
        // 비밀번호 입력
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 비밀번호 확인
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("비밀번호 확인") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // — 별명 입력란 추가
        OutlinedTextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = { Text("별명 (채팅 시 표시될 이름)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                val intent = Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                }
                launcher.launch(intent)
            }) {
                Text("학생증 업로드")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (studentIdUri != null) "파일 있음 ✅ (2단계 진행됨)" else "파일 없음")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = department,
            onValueChange = { department = it },
            label = { Text("학과 예 : 컴퓨터공학부") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = studentNumber,
            onValueChange = { studentNumber = it },
            label = { Text("학번 예 : 202512345") })

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            val uri = studentIdUri
            if (password.isBlank()) {
                message = "비밀번호를 입력해주세요."
                return@Button
            }
            if (confirmPassword.isBlank()) {
                message = "비밀번호 확인을 입력해주세요."
                return@Button
            }
            if (nickname.isBlank()) {
                message = "별명을 입력해주세요."
                return@Button
            }
            if (department.isBlank()) {
                message = "학과를 입력해주세요."
                return@Button
            }
            if (studentNumber.isBlank()) {
                message = "학번을 입력해주세요."
                return@Button
            }
            if (uri == null) {
                message = "학생증을 업로드해주세요."
                return@Button
            }
            if (!emailVerified) {
                message = "이메일 인증을 먼저 완료해주세요."
                return@Button
            }
            if (password != confirmPassword) {
                message = "비밀번호가 일치하지 않습니다."
                return@Button
            }
            viewModel.updatePassword(password) { success, error ->
                if (success) {
                    viewModel.saveUserInfo(
                        department    = department,
                        studentNumber = studentNumber,
                        nickname      = nickname      // ← NEW
                    ) { saveSuccess ->
                        if (!saveSuccess) {
                            message = "사용자 정보 저장 실패"
                            return@saveUserInfo
                        }
                        sendEmailToAdmin(context, uri, "$email / $department / $studentNumber / nickname:$nickname")
                        message = "비밀번호가 변경되고 승인 요청이 전송되었습니다."
                        onNavigateToLogin()
                    }
                } else {
                    message = "비밀번호 변경 실패: $error"
                }
            }
        }) {
            Text("승인 요청")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(message)
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text("이미 계정이 있으신가요? Log In")
        }
    }
}
