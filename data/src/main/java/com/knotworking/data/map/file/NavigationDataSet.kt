package com.knotworking.data.map.file

class NavigationDataSet {
    private var placemarks: ArrayList<Placemark?> = ArrayList<Placemark?>()
    private var currentPlacemark: Placemark? = null

    var routeData: ArrayList<String> = ArrayList()
    var routePlacemark: Placemark? = null

    fun addCurrentPlacemark() {
        placemarks.add(currentPlacemark)
    }

    fun getPlacemarks(): ArrayList<Placemark?> {
        return placemarks
    }

    fun setPlacemarks(placemarks: ArrayList<Placemark?>) {
        this.placemarks = placemarks
    }

    fun getCurrentPlacemark(): Placemark? {
        return currentPlacemark
    }

    fun setCurrentPlacemark(currentPlacemark: Placemark?) {
        this.currentPlacemark = currentPlacemark
    }
}