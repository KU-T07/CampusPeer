import androidx.compose.runtime.Composable
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import android.view.Gravity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.*
@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MapMarkerPopupDialog(
    onDismiss: () -> Unit,
    onLocationSelected: (LatLng, String?) -> Unit
) {
    val defaultLocations = listOf(
        "황소상" to LatLng(37.54312, 127.07618),
        "새천년관 앞" to LatLng(37.54313, 127.07739),
        "공학관 앞" to LatLng(37.54168, 127.07867),
        "레이크홀 편의점" to LatLng(37.53937, 127.07706),
        "도서관" to LatLng(37.54160, 127.07356)
    )
    var selectedLocation by remember { mutableStateOf<Pair<LatLng, String?>?>(null) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(37.5408, 127.0793), 15.0)
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {
            Column(Modifier.fillMaxSize()) {
                Text(
                    "위치 선택",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )

                Box(modifier = Modifier.weight(1f)) {
                    NaverMap(
                        cameraPositionState = cameraPositionState,
                        modifier = Modifier.fillMaxSize(),
                        onMapClick = { _, coord ->
                            selectedLocation = coord to null
                        }
                    ) {
                        defaultLocations.forEach { (name, position) ->
                            Marker(
                                state = MarkerState(position),
                                captionText = name,
                                onClick = {
                                    selectedLocation = position to name
                                    true
                                }
                            )
                        }
                        selectedLocation?.let { (latLng, name) ->
                            Marker(
                                state = MarkerState(latLng),
                                captionText = name ?: "선택됨",
                                iconTintColor = Color.Blue
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        selectedLocation?.let { (latLng, name) ->
                            onLocationSelected(latLng, name)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("선택 완료")
                }
            }
        }
    }
}
