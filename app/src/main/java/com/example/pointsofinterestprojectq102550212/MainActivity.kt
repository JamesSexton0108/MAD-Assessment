package com.example.pointsofinterestprojectq102550212

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.pointsofinterestprojectq102550212.ui.theme.PointsOfInterestProjectQ102550212Theme
import org.maplibre.android.geometry.LatLng


class MainActivity : ComponentActivity(), LocationListener {
    val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()

        setContent {
            MapScreen(viewModel = viewModel)
        }
    }
    fun checkPermissions() {
        val permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    startGPS()
                } else {
                    Toast.makeText(this, "GPS permission not granted", Toast.LENGTH_LONG).show()
                }
            }
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    @SuppressLint("MissingPermission")
    fun startGPS() {
        val mgr = getSystemService(LOCATION_SERVICE) as LocationManager
        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)

    }

    override fun onLocationChanged(location: Location) {
        viewModel.latLng = LatLng(location.latitude, location.longitude)
    }
}


