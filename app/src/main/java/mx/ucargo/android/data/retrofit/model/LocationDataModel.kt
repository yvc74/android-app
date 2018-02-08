package mx.ucargo.android.data.retrofit.model

import com.google.gson.annotations.SerializedName

class LocationDataModel {
    @SerializedName("name")
    var name = ""

    @SerializedName("latitude")
    var latitude: Double = 0.0

    @SerializedName("longitude")
    var longitude: Double = 0.0
}
