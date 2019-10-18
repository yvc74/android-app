package mx.ucargo.android.data.retrofit.model

import com.google.gson.annotations.SerializedName


data class LocationEventDataModel(
        @SerializedName("id") var uuid: String = "",
        @SerializedName("name") var name: String = "",
        @SerializedName("date") var date: String = "",
        @SerializedName("track") var track: TrackEventDataModel = TrackEventDataModel()
)
