package com.example.pointsofinterestprojectq102550212

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.maplibre.android.geometry.LatLng

data class PointOfInterest(
    val name: String,
    val type: String,
    val description: String,
    val latLng: LatLng
)
class MainViewModel: ViewModel() {
    var latLng = LatLng(50.9079, -1.4015)
        set(newValue) {
            field = newValue
            latLngLiveData.value = newValue
        }
    var latLngLiveData = MutableLiveData<LatLng>()

    var zoom: Double = 14.0
        set(newValue) {
            field = newValue
            zoomLiveData.value = newValue
        }
    var zoomLiveData = MutableLiveData<Double>()

    val poisList = MutableLiveData<List<PointOfInterest>>(emptyList())

    fun addPOI(poi: PointOfInterest) {
        val current = poisList.value ?: emptyList()
        poisList.value = current + poi
    }
}