package com.knotworking.domain.location

open class Location(
    @Transient
    open val latitude: Double,
    @Transient
    open val longitude: Double)
