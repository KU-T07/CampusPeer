package com.example.campuspeer.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.campuspeer.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class EmailAuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val usersRef = Firebase.database.getReference("Users")

    // 1. 이메일 인증 메일 전송 (비밀번호 없이)
    fun sendVerificationEmail(email: String, onComplete: (Boolean, String?) -> Unit) {
        val fakePassword = "temporary123!"

        auth.createUserWithEmailAndPassword(email, fakePassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.sendEmailVerification()
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    // 2. 이메일 인증 여부 확인
    fun checkEmailVerified(onResult: (Boolean) -> Unit) {
        auth.currentUser?.reload()?.addOnSuccessListener {
            onResult(auth.currentUser?.isEmailVerified == true)
        } ?: onResult(false)
    }

    // 3. 학생증 이미지 업로드
//    fun uploadStudentIdImage(uri: Uri, onComplete: (Boolean, String?) -> Unit) {
//        val uid = auth.currentUser?.uid ?: return onComplete(false, "사용자 없음")
//        val ref = storageRef.child("student_ids/$uid.jpg")
//
//        ref.putFile(uri).continueWithTask { task ->
//            if (!task.isSuccessful) throw task.exception!!
//            ref.downloadUrl
//        }.addOnSuccessListener { url ->
//            onComplete(true, url.toString())
//        }.addOnFailureListener {
//            onComplete(false, null)
//        }
//    }

    // 4. 사용자 정보 저장 (verified = false)
    fun saveUserInfo(department: String, studentNumber: String, onResult: (Boolean) -> Unit) {
        val user = auth.currentUser ?: return onResult(false)
        val userData = UserData(
            uid = user.uid,
            email = user.email ?: "",
            department = department,
            studentNumber = studentNumber,
            verified = false // 최초 저장 시 미승인 상태
        )

        usersRef.child(user.uid).setValue(userData)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun updatePassword(newPassword: String, onResult: (Boolean, String?) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            user.updatePassword(newPassword)
                .addOnSuccessListener { onResult(true, null) }
                .addOnFailureListener { e -> onResult(false, e.message) }
        } else {
            onResult(false, "로그인된 사용자가 없습니다.")
        }
    }

    // 기존 사용자 이메일+비번으로 로그인
    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val uid = auth.currentUser?.uid ?: return@addOnSuccessListener
                usersRef.child(uid).get()
                    .addOnSuccessListener { snapshot ->
                        val verified = snapshot.child("verified").getValue(Boolean::class.java) ?: false
                        if (verified) {
                            onResult(true, null)
                        } else {
                            // 승인 안 된 사용자
                            auth.signOut() // 인증만 해놓고 로그아웃 처리
                            onResult(false, "관리자 승인이 필요합니다.")
                        }
                    }
                    .addOnFailureListener { e ->
                        auth.signOut()
                        onResult(false, "유저 정보 조회 실패: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                onResult(false, e.message)
            }
    }

    fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

}
