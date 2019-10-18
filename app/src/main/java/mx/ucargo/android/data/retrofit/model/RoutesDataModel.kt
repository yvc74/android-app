package mx.ucargo.android.data.retrofit.model

import com.google.gson.annotations.SerializedName


data class RoutesDataModel(
        @SerializedName("routes") var routes: List<RouteDataModel> = emptyList()
)
