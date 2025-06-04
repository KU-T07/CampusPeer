package com.example.campuspeer.uicomponent

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.campuspeer.itemBoard.CategorySelector // CategorySelector 임포트

@Composable
fun ColumnScope.DrawerContent(modifier: Modifier = Modifier) {
    // DrawerContent 내에서 선택된 카테고리를 관리할 상태 변수
    var selectedCategoryInDrawer by remember { mutableStateOf("선택 안됨") }

    // 카테고리 선택 부분
    // Modifier를 사용하여 DrawerContent 내에서 CategorySelector의 패딩 등을 조절
    CategorySelector(
        selectedCategory = selectedCategoryInDrawer,
        onCategorySelected = { category ->
            selectedCategoryInDrawer = category
            // 선택된 카테고리를 ViewModel이나 다른 상태 홀더로 전달하는 로직 추가
            println("Drawer에서 선택된 카테고리: $category")
        },
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp) // 드로어 내에서 상하 패딩 추가
    )

    // 드로어의 다른 내용 (예시)
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("메뉴 항목 1", modifier = Modifier.padding(vertical = 8.dp))
        Text("메뉴 항목 2", modifier = Modifier.padding(vertical = 8.dp))
        Text("현재 드로어 카테고리: $selectedCategoryInDrawer", modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Preview(showBackground = true, widthDp = 280, heightDp = 480) // 드로어 사이즈에 맞게 미리보기 설정
@Composable
fun PreviewDrawerContent() {
    // ColumnScope 컨텍스트를 제공하기 위해 Column 내에서 호출
    Column(modifier = Modifier.fillMaxSize()) {
        DrawerContent()
    }
}