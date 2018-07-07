package mx.ucargo.android.data.retrofit.model

import com.google.gson.annotations.SerializedName

data class LocationDataModel(
        @SerializedName("schedule") var schedule: String = "",
        @SerializedName("name") var name: String = "",
        @SerializedName("latitude") var latitude: Double = 0.0,
        @SerializedName("longitude") var longitude: Double = 0.0,
        @SerializedName("address") var address: String = "",
        @SerializedName("label") var label: String = ""
)
