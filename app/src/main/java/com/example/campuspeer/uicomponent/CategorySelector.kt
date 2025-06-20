package com.example.campuspeer.uicomponent

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.campuspeer.model.Category


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelector(
    selectedCategory: Category, // Category enum 타입으로 변경
    onCategorySelected: (Category) -> Unit, // Category enum 타입으로 변경
    modifier: Modifier = Modifier
) {
    val categories = Category.entries.toList() // Category enum의 모든 값 가져오기
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
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
                .background(Color(0xFFFFFFFF))
        ) {
            OutlinedTextField(
                value = selectedCategory.label, // enum의 label 사용
                onValueChange = { /* 읽기 전용이므로 직접 변경하지 않음 */ },
                readOnly = true,
                label = { Text("카테고리") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .background(Color(0xFFFFFFFF))
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier.background(Color(0xFFFFFFFF))
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.label) }, // enum의 label 사용
                        onClick = {
                            onCategorySelected(category) // enum 객체 전달
                            expanded = false
                        },
                        modifier.background(Color(0xFFFFFFFF))
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategorySelectorPreview() {
    var selectedCategory by remember { mutableStateOf(Category.ELECTRONICS) }
    CategorySelector(
        selectedCategory = selectedCategory,
        onCategorySelected = { category -> selectedCategory = category }
    )
}