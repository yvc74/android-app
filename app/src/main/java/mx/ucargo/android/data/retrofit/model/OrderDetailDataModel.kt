package mx.ucargo.android.data.retrofit.model

import com.google.gson.annotations.SerializedName

data class OrderDetailDataModel(
        @SerializedName("label") var label: String = "",
        @SerializedName("url") var url: String = "",
        @SerializedName("value") var value: String = ""
)
