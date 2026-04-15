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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.keepScreenOn
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pointsofinterestprojectq102550212.ui.theme.PointsOfInterestProjectQ102550212Theme
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.Style
import org.ramani.compose.CameraPosition
import org.ramani.compose.MapLibre
import kotlin.toString
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.ramani.compose.Symbol


class MainActivity : ComponentActivity(), LocationListener {
    val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()

        setContent {
            PointsOfInterestProjectQ102550212Theme {
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()

                Scaffold() { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "mapScreen",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("mapScreen") {
                            MapScreen(navController)
                        }
                        composable("addPOIScreen") {
                            AddPOIScreen(
                                currentLatLng = viewModel.latLng,
                                returnToMapScreenCallback = { navController.popBackStack() })
                        }
                    }
                }
            }
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
    @Composable
    fun MapScreen(navController: NavController) {
        var currentPosition by remember { mutableStateOf(viewModel.latLng) }
        var zoom by remember { mutableDoubleStateOf(viewModel.zoom) }
        var pois by remember { mutableStateOf(viewModel.poisList.value ?: emptyList()) }

        viewModel.latLngLiveData.observe(this) {
            currentPosition = it
        }

        viewModel.zoomLiveData.observe(this) {
            zoom = it
        }

        viewModel.poisList.observe(this) {
            pois = it
        }

        val styleBuilder = Style.Builder()
            .fromUri("https://tiles.openfreemap.org/styles/bright")

        Column{
            MapLibre(
                modifier = Modifier.fillMaxWidth().weight(1f),
                styleBuilder = styleBuilder,
                cameraPosition = CameraPosition(
                    target = currentPosition,
                    zoom = zoom
                )
            ) {
                pois.forEach { poi ->
                    Symbol(center = poi.latLng)
                }
            }
            Button(onClick =  { navController.navigate("addPOIScreen") }) {
                Text("Go to Add POI Screen")
            }
        }

    }

    @Composable
    fun AddPOIScreen(currentLatLng: LatLng, returnToMapScreenCallback: () -> Unit) {

        var nameText by remember { mutableStateOf("")}
        var typeText by remember { mutableStateOf("")}
        var countryText by remember { mutableStateOf("")}
        var regionText by remember { mutableStateOf("")}
        var descriptionText by remember { mutableStateOf("")}
        var errorMessage by remember { mutableStateOf("") }

        Column() {
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage)
            }
            TextField(
                modifier = Modifier.padding(8.dp),
                value = nameText,
                onValueChange = {nameText = it},
                label = {Text("Enter the name of the location")}
            )

            TextField(
                modifier = Modifier.padding(8.dp),
                value = typeText,
                onValueChange = {typeText = it},
                label = {Text("Enter the type (pub, restaurant, hotel etc.) of the location")}
            )

            TextField(
                modifier = Modifier.padding(8.dp),
                value = countryText,
                onValueChange = { countryText = it },
                label = { Text("Enter the country") }
            )
            TextField(
                modifier = Modifier.padding(8.dp),
                value = regionText,
                onValueChange = { regionText = it },
                label = { Text("Enter the region") }
            )

            TextField(
                modifier = Modifier.padding(8.dp),
                value = descriptionText,
                onValueChange = {descriptionText = it},
                label = {Text("Enter a brief description of the location")}
            )
            Row {
                Button(modifier = Modifier.weight(1f),
                onClick = {
                    if (nameText.isBlank() || typeText.isBlank() ||
                        countryText.isBlank() || regionText.isBlank() ||
                        descriptionText.isBlank()) {
                        errorMessage = "Please fill in all fields before adding."
                    } else {

                        val poi = PointOfInterest(
                            name = nameText.trim(),
                            type = typeText.trim(),
                            country = countryText.trim(),
                            region = regionText.trim(),
                            description = descriptionText.trim(),
                            latLng = currentLatLng
                        )
                    viewModel.addPOI(poi)
                    }

                }) {Text("Add marker to map.")}

                Button(modifier = Modifier.weight(1f),
                    onClick = {
                    returnToMapScreenCallback()
                }) { Text("Return") }
            }
        }




    }

}




