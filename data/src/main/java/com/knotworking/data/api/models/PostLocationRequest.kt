package com.knotworking.data.api.models

data class PostLocationRequest(
    val location: Array<Double>,
    val markerText: String,
    val updatedAt: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PostLocationRequest) return false

        if (!location.contentEquals(other.location)) return false
        if (markerText != other.markerText) return false
        if (updatedAt != other.updatedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = location.contentHashCode()
        result = 31 * result + markerText.hashCode()
        result = 31 * result + updatedAt.hashCode()
        return result
    }
}
