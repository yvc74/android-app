package mx.ucargo.android.entity

import com.google.android.gms.maps.model.LatLng


data class Route (
        var points: List<LatLng>
)