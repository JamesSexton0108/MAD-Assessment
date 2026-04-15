package com.example.pointsofinterestprojectq102550212

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface poiDAO {
    @Insert
    fun insert(poi: POIEntity)

    @Query("SELECT * FROM pointsofinterest")
    fun getAll(): LiveData<List<POIEntity>>
}
