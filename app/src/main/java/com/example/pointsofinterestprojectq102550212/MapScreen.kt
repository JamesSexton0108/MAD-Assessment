package com.example.pointsofinterestprojectq102550212

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.Style
import org.ramani.compose.CameraPosition
import org.ramani.compose.MapLibre

@Composable
fun MapScreen(viewModel: MainViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current

    var currentPosition by remember { mutableStateOf(viewModel.latLng) }
    var zoom by remember { mutableDoubleStateOf(viewModel.zoom) }

    viewModel.latLngLiveData.observe(lifecycleOwner) {
        currentPosition = it
    }

    viewModel.zoomLiveData.observe(lifecycleOwner) {
        zoom = it
    }

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