package mx.ucargo.android.data.retrofit.model

import com.google.gson.annotations.SerializedName


data class TrackEventDataModel(
        @SerializedName("longitude") var longitude: Double = 0.0,
        @SerializedName("latitude") var latitude: Double = 0.0,
        @SerializedName("bearing") var bearing: Float = 0.0F
)