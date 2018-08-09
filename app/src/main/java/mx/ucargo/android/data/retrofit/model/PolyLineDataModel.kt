package mx.ucargo.android.data.retrofit.model

import com.google.gson.annotations.SerializedName


data class PolyLineDataModel (
        @SerializedName("points") var points:String = ""
)