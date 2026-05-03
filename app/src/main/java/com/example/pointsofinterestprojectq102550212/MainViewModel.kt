package com.example.pointsofinterestprojectq102550212

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.maplibre.android.geometry.LatLng

data class PointOfInterest(
    val name: String,
    val type: String,
    val country: String,
    val region: String,
    val description: String,
    val latLng: LatLng
)

data class POIJson(
    val id: Long,
    val name: String,
    val type: String,
    val country: String,
    val region: String,
    val lon: Double,
    val lat: Double,
    val description: String,
    val recommendations: Int
)
class MainViewModel(app: Application): AndroidViewModel(app) {

    var db = POIDatabase.getDatabase(app)
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

    var poisList: LiveData<List<PointOfInterest>> = db.poiDao().getAll().map { entities ->
        entities.map { entity ->
            PointOfInterest(
                name = entity.name,
                type = entity.type,
                country = entity.country,
                region = entity.region,
                description = entity.description,
                latLng = LatLng(entity.lat, entity.lon)
            )
        }
    }

    fun searchByType(type: String): LiveData<List<PointOfInterest>> {
        return db.poiDao().getByType(type).map { entities ->
            entities.map { entity ->
                PointOfInterest(
                    name = entity.name,
                    type = entity.type,
                    country = entity.country,
                    region = entity.region,
                    description = entity.description,
                    latLng = LatLng(entity.lat, entity.lon)
                )
            }
        }
    }

    fun savePOIWithId(poi: PointOfInterest, id: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val entity = POIEntity(
                    id = id,
                    name = poi.name,
                    type = poi.type,
                    country = poi.country,
                    region = poi.region,
                    lat = poi.latLng.latitude,
                    lon = poi.latLng.longitude,
                    description = poi.description,
                    recommendations = 0
                )
                db.poiDao().insert(entity)
            }
        }
    }

    fun savePOIsFromWeb(pois: List<POIJson>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                pois.forEach { poi ->
                    if (db.poiDao().getById(poi.id) == null) {
                        val entity = POIEntity(
                            id = poi.id,
                            name = poi.name,
                            type = poi.type,
                            country = poi.country,
                            region = poi.region,
                            lat = poi.lat,
                            lon = poi.lon,
                            description = poi.description,
                            recommendations = poi.recommendations
                        )
                        db.poiDao().insert(entity)
                    }
                }
            }
        }
    }
}