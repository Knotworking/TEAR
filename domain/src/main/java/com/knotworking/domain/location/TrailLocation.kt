package com.knotworking.domain.location

data class TrailLocation(
    override val latitude: Double,
    override val longitude: Double,
    val kmProgress: Double,
    val percentageProgress: Double,
    val metresToTrail: Double?
) :
    Location(latitude, longitude)
