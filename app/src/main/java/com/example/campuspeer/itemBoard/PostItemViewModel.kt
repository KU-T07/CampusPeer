package com.example.campuspeer.itemBoard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuspeer.model.PostItem
import com.example.campuspeer.model.UserData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PostItemViewModel: ViewModel() {
    private val repo = PostItemRepository()
    val posts =
        repo.getPosts().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList<PostItem>())
    private val _users = MutableStateFlow<List<UserData>>(emptyList())
    val users = _users

    init {
        viewModelScope.launch {
            posts.collect {
                Log.d("PostViewModel", "받은 posts 개수: ${it.size}")
            }
        }
        loadUsers()
    }

    private fun loadUsers() {
        val usersRef = FirebaseDatabase.getInstance().getReference("Users")
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = snapshot.children.mapNotNull { child ->
                    val uid = child.key ?: return@mapNotNull null
                    val email = child.child("email").getValue(String::class.java) ?: ""
                    val department = child.child("department").getValue(String::class.java) ?: ""
                    val studentNumber = child.child("studentNumber").getValue(String::class.java) ?: ""
                    val verified = child.child("verified").getValue(Boolean::class.java) ?: false

                    UserData(
                        uid = uid,
                        email = email,
                        department = department,
                        studentNumber = studentNumber,
                        verified = verified
                    )
                }

                _users.value = userList

                Log.d("유저확인", "📋 불러온 사용자 수: ${userList.size}")
                userList.forEach { user ->
                    Log.d("유저확인", "🧑 uid=${user.uid}, department=${user.department}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("PostViewModel", "유저 불러오기 실패: ${error.message}")
            }
        })
    }


}