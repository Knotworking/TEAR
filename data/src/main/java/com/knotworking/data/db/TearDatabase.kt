package com.knotworking.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.knotworking.data.map.KmMarker
import com.knotworking.data.map.KmMarkerDao

@Database(entities = [KmMarker::class], version = 1)
abstract class TearDatabase : RoomDatabase() {

    abstract fun kmMarkerDao(): KmMarkerDao
}