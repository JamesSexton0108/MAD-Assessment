package com.example.pointsofinterestprojectq102550212

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface poiDAO {
    @Insert
    fun insert(poi: POIEntity)
}