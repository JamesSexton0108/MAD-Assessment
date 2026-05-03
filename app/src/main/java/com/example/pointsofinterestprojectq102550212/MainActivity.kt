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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
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
import com.github.kittinunf.fuel.httpGet
import org.ramani.compose.Circle
import org.ramani.compose.Symbol
import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import kotlinx.coroutines.launch


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
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

                var statusMessage by remember { mutableStateOf("") }

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = false,
                    drawerContent = {
                        ModalDrawerSheet {
                            NavigationDrawerItem(
                                label = { Text("Map") },
                                selected = false,
                                onClick = {
                                    coroutineScope.launch { drawerState.close() }
                                    navController.navigate("mapScreen")
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("Add Point of Interest") },
                                selected = false,
                                onClick = {
                                    coroutineScope.launch { drawerState.close() }
                                    navController.navigate("addPOIScreen")
                                }
                            )

                            NavigationDrawerItem(
                                label = { Text("Download POIs from Web") },
                                selected = false,
                                onClick = {
                                    coroutineScope.launch { drawerState.close() }
                                    statusMessage = "Loading POIs from web..."
                                    "http://10.0.2.2:3000/poi/all"
                                        .httpGet()
                                        .responseObject<List<POIJson>> { _, _, result ->
                                            when (result) {
                                                is Result.Success -> {
                                                    val webPois = result.get()
                                                    viewModel.savePOIsFromWeb(webPois)
                                                    statusMessage = "Loaded ${webPois.size} POIs from web."
                                                }
                                                is Result.Failure -> {
                                                    statusMessage = "Error: ${result.error.message}"
                                                }
                                            }
                                        }
                                }
                            )
                        }
                    }
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary
                                ),
                                title = { Text("Points of Interest") },
                                actions = {
                                    IconButton(onClick = {
                                        coroutineScope.launch {
                                            if (drawerState.isClosed) {
                                                drawerState.open()
                                            } else {
                                                drawerState.close()
                                            }
                                        }
                                    }) {
                                        Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                                    }
                                }
                            )
                        },
                        bottomBar = {
                            NavigationBar {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Home, contentDescription = "Map") },
                                    label = { Text("Map") },
                                    onClick = { navController.navigate("mapScreen") },
                                    selected = false
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Add, contentDescription = "Add POI") },
                                    label = { Text("Add POI") },
                                    onClick = { navController.navigate("addPOIScreen") },
                                    selected = false
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                                    label = { Text("Search") },
                                    onClick = { navController.navigate("mapScreen") },
                                    selected = false
                                )
                            }
                        },
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = { navController.navigate("addPOIScreen") },
                                content = {
                                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add POI")
                                }
                            )
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "mapScreen",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("mapScreen") {
                                MapScreen(navController, statusMessage) { statusMessage = it }
                            }
                            composable("addPOIScreen") {
                                AddPOIScreen(
                                    currentLatLng = viewModel.latLng,
                                    returnToMapScreenCallback = { navController.popBackStack() }
                                )
                            }
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
    fun MapScreen(
        navController: NavController,
        statusMessage: String,
        onStatusMessage: (String) -> Unit
    ) {
        var currentPosition by remember { mutableStateOf(viewModel.latLng) }
        var zoom by remember { mutableDoubleStateOf(viewModel.zoom) }
        var pois by remember { mutableStateOf(viewModel.poisList.value ?: emptyList()) }
        var searchResults by remember { mutableStateOf(listOf<PointOfInterest>()) }
        var typeSearchText by remember { mutableStateOf("") }
        var selectedPOI by remember { mutableStateOf<PointOfInterest?>(null) }

        viewModel.latLngLiveData.observe(this) { currentPosition = it }
        viewModel.zoomLiveData.observe(this) { zoom = it }
        viewModel.poisList.observe(this) { pois = it }

        val styleBuilder = Style.Builder()
            .fromUri("https://tiles.openfreemap.org/styles/bright")

        Column {
            MapLibre(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                styleBuilder = styleBuilder,
                cameraPosition = CameraPosition(target = currentPosition, zoom = zoom)
            ) {
                pois.forEach { poi ->
                    Symbol(
                        center = poi.latLng,
                        isDraggable = false,
                        onClick = { selectedPOI = poi }
                    )
                }
                searchResults.forEach { poi ->
                    Circle(
                        center = poi.latLng,
                        radius = 15f,
                        isDraggable = false,
                        color = "Blue",
                        onClick = { selectedPOI = poi }
                    )
                }
            }

            if (statusMessage.isNotEmpty()) {
                Text(text = statusMessage, modifier = Modifier.padding(8.dp))
            }

            Row(modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 80.dp)) {
                TextField(
                    modifier = Modifier.weight(1f),
                    value = typeSearchText,
                    onValueChange = { typeSearchText = it },
                    label = { Text("Search by type") }
                )
                Button(
                    modifier = Modifier.padding(start = 8.dp),
                    onClick = {
                        if (typeSearchText.isNotBlank()) {
                            viewModel.searchByType(typeSearchText.trim())
                                .observe(this@MainActivity) {
                                    searchResults = it
                                }
                        }
                    }
                ) {
                    Text("Search")
                }
            }
        }

        selectedPOI?.let { poi ->
            POIDetailDialog(poi = poi, onDismiss = { selectedPOI = null })
        }
    }

    @Composable
    fun POIDetailDialog(poi: PointOfInterest, onDismiss: () -> Unit) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(poi.name) },
            text = {
                Column {
                    Text("Type: ${poi.type}")
                    Text("Country: ${poi.country}")
                    Text("Region: ${poi.region}")
                    Text("Description: ${poi.description}")
                    Text("Latitude: ${poi.latLng.latitude}")
                    Text("Longitude: ${poi.latLng.longitude}")
                }
            },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("Close")
                }
            }
        )
    }

    @Composable
    fun AddPOIScreen(currentLatLng: LatLng, returnToMapScreenCallback: () -> Unit) {
        var nameText by remember { mutableStateOf("") }
        var typeText by remember { mutableStateOf("") }
        var countryText by remember { mutableStateOf("") }
        var regionText by remember { mutableStateOf("") }
        var descriptionText by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf("") }

        Column {
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage)
            }
            TextField(
                modifier = Modifier.padding(8.dp),
                value = nameText,
                onValueChange = { nameText = it },
                label = { Text("Enter the name of the location") }
            )
            TextField(
                modifier = Modifier.padding(8.dp),
                value = typeText,
                onValueChange = { typeText = it },
                label = { Text("Enter the type (pub, restaurant, hotel etc.) of the location") }
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
                onValueChange = { descriptionText = it },
                label = { Text("Enter a brief description of the location") }
            )

            Row {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        if (nameText.isBlank() || typeText.isBlank() ||
                            countryText.isBlank() || regionText.isBlank() ||
                            descriptionText.isBlank()
                        ) {
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

                            val postData = listOf(
                                "name" to poi.name,
                                "type" to poi.type,
                                "description" to poi.description,
                                "lat" to poi.latLng.latitude,
                                "lon" to poi.latLng.longitude
                            )

                            "http://10.0.2.2:3000/poi/create"
                                .httpPost(postData)
                                .response { _, _, result ->
                                    when (result) {
                                        is Result.Success -> {
                                            val newId = result.get().decodeToString().toLong()
                                            viewModel.savePOIWithId(poi, newId)
                                            returnToMapScreenCallback()
                                        }
                                        is Result.Failure -> {
                                            errorMessage = "Error saving to server: ${result.error.message}"
                                        }
                                    }
                                }
                        }
                    }
                ) { Text("Add marker to map.") }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { returnToMapScreenCallback() }
                ) { Text("Return") }
            }
        }
    }
}



