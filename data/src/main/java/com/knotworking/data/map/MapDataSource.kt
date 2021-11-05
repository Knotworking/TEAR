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
        if (allMarkers.isEmpty()) {
            database.kmMarkerDao().insertAll(listOf(
                KmMarker(0, 0, 27.899409, 42.704819),
                KmMarker(1, 1, 27.893293661, 42.710019387),
                KmMarker(2, 2, 27.884982483, 42.715500429),
            ))
        }
    }
}