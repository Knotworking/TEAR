package com.knotworking.data.route

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface KmMarkerDao {

    @Query("SELECT * FROM kmmarker")
    suspend fun getAll(): List<KmMarker>

    @Query("SELECT * FROM kmmarker WHERE km = :km")
    suspend fun getForKm(km: Int): List<KmMarker>

    @Insert
    suspend fun insertAll(kmMarkers: List<KmMarker>)

    @Delete
    suspend fun delete(kmMarker: KmMarker)
}