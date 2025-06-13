package com.example.campuspeer.uicomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import com.example.campuspeer.model.Category

@Composable
fun ColumnScope.DrawerContent(modifier: Modifier = Modifier) {
    // DrawerContent 내에서 선택된 카테고리를 관리할 상태 변수
    var selectedCategoryInDrawer by remember { mutableStateOf(Category.ELECTRONICS) } // 기본 카테고리 설정

    // 여기에 드로어의 다른 UI 요소들이 들어갈 수 있습니다.
    // 예를 들어, 헤더, 다른 메뉴 항목 등
    Column(modifier = modifier.fillMaxWidth()) { // DrawerContent에 전달된 Modifier를 적용
        Text(
            text = "메뉴 헤더",
            modifier = Modifier.padding(16.dp)
        )

        // 카테고리 선택 부분
        CategorySelector(
            selectedCategory = selectedCategoryInDrawer,
            onCategorySelected = { category ->
                selectedCategoryInDrawer = category
                // 선택된 카테고리를 ViewModel이나 다른 상태 홀더로 전달하는 로직 추가
                println("Drawer에서 선택된 카테고리: $category")
            },
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp) // 드로어 내에서 상하 패딩 추가
        )

        // 드로어의 다른 내용 (예시)
        Text(
            text = "현재 선택된 카테고리: ${selectedCategoryInDrawer.label}",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Text(
            text = "메뉴 항목 1",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Text(
            text = "메뉴 항목 2",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
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