package mx.ucargo.android.entity

data class Location(
        var name: String = "",
        var address: String = "",
        var label: String = "",
        var schedule: String = "",
        var latitude: Double = 0.0,
        var longitude: Double = 0.0
)
