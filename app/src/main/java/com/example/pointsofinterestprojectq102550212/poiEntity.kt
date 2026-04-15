package com.example.pointsofinterestprojectq102550212

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pointsofinterest")
data class POIEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "region") val region: String,
    @ColumnInfo(name = "lon") val lon: Double,
    @ColumnInfo(name = "lat") val lat: Double,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "recommendations") val recommendations: Int = 0
)
