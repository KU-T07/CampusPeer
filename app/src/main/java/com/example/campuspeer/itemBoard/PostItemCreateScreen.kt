package com.example.campuspeer.itemBoard

import MapMarkerPopupDialog
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.campuspeer.model.Category
import com.example.campuspeer.model.PostItem
import com.example.campuspeer.model.Routes
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.naver.maps.geometry.LatLng


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItemCreateScreen(
    navController: NavController,
    onBackClick: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var showMapDialog by remember { mutableStateOf(false) }
    var selectedLatLng by remember { mutableStateOf<LatLng?>(null) }
    var selectedName by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }
    val context = LocalContext.current
    val storageRef = Firebase.storage.reference
    val firestore = Firebase.firestore

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text("게시글 등록", style = MaterialTheme.typography.titleLarge)
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { imagePickerLauncher.launch("image/*") },
            modifier = Modifier.size(140.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "선택된 이미지",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(Icons.Default.Add, contentDescription = "이미지 선택", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text("제목") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            placeholder = { Text("가격") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("설명") },
            modifier = Modifier.fillMaxWidth().height(120.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        val categoryList = Category.values().toList()
        var selectedCategory by remember { mutableStateOf<Category?>(null) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedCategory?.label ?: "카테고리 선택하기",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded,
                        modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.SecondaryEditable))
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categoryList.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.label) },
                        onClick = {
                            selectedCategory = category
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        selectedLatLng?.let {
            Text(
                text = "위도: %.5f, 경도: %.5f".format(it.latitude, it.longitude),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )
        }

        TextButton(onClick = { showMapDialog = true }) {
            Text("위치 선택")
        }

        if (showMapDialog) {
            MapMarkerPopupDialog(
                onDismiss = { showMapDialog = false },
                onLocationSelected = { latLng, name ->
                    selectedLatLng = latLng
                    selectedName = name.orEmpty()
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val postId = firestore.collection("posts").document().id

                val uploadAndCreatePost = {
                    val post = PostItem(
                        id = postId,
                        title = title,
                        price = price.toIntOrNull() ?: 0,
                        description = description,
                        category = selectedCategory ?: Category.ETC,
                        imageUrl = "",
                        status = "거래가능",
                        timestamp = System.currentTimeMillis(),
                        sellerId = Firebase.auth.currentUser?.uid ?: "unknown",
                        location = selectedName,
                        latlng = selectedLatLng ?: LatLng(37.54168, 127.07867)
                    )

                    if (imageUri != null) {
                        val filename = "images/${postId}.jpg"
                        val imageRef = storageRef.child(filename)
                        imageRef.putFile(imageUri!!)
                            .addOnSuccessListener {
                                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                    val finalPost = post.copy(imageUrl = downloadUrl.toString())
                                    val repository = PostItemRepository()
                                    repository.addPost(
                                        finalPost,
                                        onSuccess = {
                                            navController.navigate(Routes.PostItemList.route) {
                                                popUpTo(Routes.PostItemCreate.route) { inclusive = true }
                                            }
                                        },
                                        onFailure = {
                                            Toast.makeText(context, "등록 실패", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        val repository = PostItemRepository()
                        repository.addPost(
                            post,
                            onSuccess = {
                                navController.navigate(Routes.PostItemList.route) {
                                    popUpTo(Routes.PostItemCreate.route) { inclusive = true }
                                }
                            },
                            onFailure = {
                                Toast.makeText(context, "등록 실패", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }

                uploadAndCreatePost()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2979FF)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("등록하기", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPostItemCreateScreen() {
    val navController = rememberNavController()
    PostItemCreateScreen(
        navController = navController,
        onBackClick = {}
    )
}
