package com.knotworking.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.knotworking.data.route.KmMarker
import com.knotworking.data.route.KmMarkerDao

@Database(entities = [KmMarker::class], version = 1)
abstract class TearDatabase : RoomDatabase() {

    abstract fun kmMarkerDao(): KmMarkerDao
}