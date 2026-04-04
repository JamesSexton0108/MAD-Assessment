package com.example.pointsofinterestprojectq102550212

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.Style
import org.ramani.compose.CameraPosition
import org.ramani.compose.MapLibre

@Composable
fun MapScreen(
    viewModel: MainViewModel
) {
    val currentPosition: LatLng by remember { mutableStateOf(viewModel.latLng) }
    val zoom: Double by remember { mutableDoubleStateOf(viewModel.zoom) }

    val styleBuilder = Style.Builder()
        .fromUri("https://tiles.openfreemap.org/styles/bright")

    MapLibre(
        modifier = Modifier.fillMaxSize(),
        styleBuilder = styleBuilder,
        cameraPosition = CameraPosition(
            target = currentPosition,
            zoom = zoom
        )
    ) {
    }
}