package com.example.campuspeer.itemBoard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelector(
    selectedCategory: String, // 현재 선택된 카테고리
    onCategorySelected: (String) -> Unit, // 카테고리 선택 시 호출될 콜백
    modifier: Modifier = Modifier // 외부에서 Modifier를 전달받을 수 있도록 추가
) {
    val categories = listOf("전자기기", "도서", "의류", "생활용품", "기타") // 예시 카테고리 목록
    var expanded by remember { mutableStateOf(false) } // 드롭다운 메뉴 확장 상태

    Column(modifier = modifier.fillMaxWidth()) { // 외부 Modifier 적용
        Text(
            text = "category",
            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = { /* 읽기 전용이므로 직접 변경하지 않음 */ },
                readOnly = true,
                label = { Text("카테고리") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            onCategorySelected(category)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategorySelectorPreview() {
    var selectedCategory by remember { mutableStateOf("전자기기") }
    CategorySelector(
        selectedCategory = selectedCategory,
        onCategorySelected = { category -> selectedCategory = category }
    )
}