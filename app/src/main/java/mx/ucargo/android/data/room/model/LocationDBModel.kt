package mx.ucargo.android.data.room.model

data class LocationDBModel(
        var name: String = "",
        var address: String = "",
        var schedule: String = "",
        var latitude: Double = 0.0,
        var longitude: Double = 0.0
)
