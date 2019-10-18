package mx.ucargo.android.data.retrofit.model

import com.google.gson.annotations.SerializedName


data class RouteDataModel(
        @SerializedName("overview_polyline") var polyline: PolyLineDataModel
)