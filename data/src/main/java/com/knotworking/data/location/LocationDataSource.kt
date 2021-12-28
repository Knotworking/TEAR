package com.knotworking.data.location

import android.content.SharedPreferences
import com.google.gson.Gson
import com.knotworking.domain.location.TrailLocation

interface LocationDataSource {
    suspend fun getLastTrailLocation(): TrailLocation?
    suspend fun storeLastTrailLocation(trailLocation: TrailLocation)
}

class LocalLocationDataSource(private val sharedPrefs: SharedPreferences) : LocationDataSource {
    private val trailLocationKey = "trail_location"

    override suspend fun getLastTrailLocation(): TrailLocation? {
        val json = sharedPrefs.getString(trailLocationKey, null)
        json?.let {
            val gson = Gson()
            return gson.fromJson(json, TrailLocation::class.java)
        }
        return null
    }

    override suspend fun storeLastTrailLocation(trailLocation: TrailLocation) {
        val gson = Gson()
        val json = gson.toJson(trailLocation)
        sharedPrefs.edit().putString(trailLocationKey, json).apply()
    }

}