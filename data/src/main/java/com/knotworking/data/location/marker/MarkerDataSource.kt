package com.knotworking.data.location.marker

import android.content.SharedPreferences

interface MarkerDataSource {
    suspend fun getMarkerText(): String
    suspend fun setMarkerText(text: String)
}

class LocalMarkerDataSource(private val sharedPrefs: SharedPreferences) : MarkerDataSource {
    private val markerTextKey = "marker_text"

    override suspend fun getMarkerText(): String {
        return sharedPrefs.getString(markerTextKey, "") ?: ""
    }

    override suspend fun setMarkerText(text: String) {
        sharedPrefs.edit().putString(markerTextKey, text).apply()
    }
}