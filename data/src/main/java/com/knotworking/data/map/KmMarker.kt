package com.knotworking.data.map

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KmMarker(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "km") val km: Int,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "latitude") val latitude: Double
)
