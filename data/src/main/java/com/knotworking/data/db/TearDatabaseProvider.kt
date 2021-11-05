package com.knotworking.data.db

import android.content.Context
import androidx.room.Room

object TearDatabaseProvider {
    fun buildDatabase(context: Context) = Room.databaseBuilder(
        context.applicationContext,
        TearDatabase::class.java,
        "tear_db"
    ).build()
}