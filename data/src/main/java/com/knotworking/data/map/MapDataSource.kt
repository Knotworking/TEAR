package com.knotworking.data.map

import android.util.Log
import com.knotworking.data.db.TearDatabase

interface MapDataSource {
    //TODO if it's a DB, no need to read from file
    suspend fun loadMapData()
}

// TODO can I just inject the relevant DAO?
class LocalMapDataSource(private val database: TearDatabase) : MapDataSource {

    override suspend fun loadMapData() {
        val allMarkers = database.kmMarkerDao().getAll()
        Log.i("TAG", "loadMapData: ${allMarkers.size}")
    }
}