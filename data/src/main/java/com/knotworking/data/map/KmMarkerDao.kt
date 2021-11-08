package com.knotworking.data.map

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface KmMarkerDao {

    @Query("SELECT * FROM kmmarker")
    suspend fun getAll(): List<KmMarker>

    /**
     * Examples
     */

    @Insert
    suspend fun insertAll(kmMarkers: List<KmMarker>)

    @Delete
    suspend fun delete(kmMarker: KmMarker)
}