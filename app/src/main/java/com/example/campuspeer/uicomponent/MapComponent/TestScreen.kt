package com.example.campuspeer.uicomponents.MapComponent

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng

@Composable
fun TestScreen(modifier: Modifier = Modifier) {
    // 선택된 위치 상태 관리
    var selectedLatLng by remember { mutableStateOf<LatLng?>(null) }
    var selectedName by remember { mutableStateOf<String?>(null) }
    // 기본 위치 리스트


    Column(modifier = modifier.fillMaxSize()) {
        // 선택용 지도: 위쪽 절반
        Box(modifier = Modifier
            .weight(2f)
            .fillMaxWidth()) {
            MapMarkerSelectScreen(
                onLocationSelected = { latLng, name ->
                    selectedLatLng = latLng
                    selectedName = name
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 출력용 지도: 아래쪽 절반
        Box(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()) {
            selectedLatLng?.let { latLng ->
                MapMarkerDisplayScreen(
                    location = latLng,
                    locationName = selectedName
                )
            }
        }
    }
}
