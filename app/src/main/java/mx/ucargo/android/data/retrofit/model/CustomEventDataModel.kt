package mx.ucargo.android.data.retrofit.model

import com.google.gson.annotations.SerializedName

data class CustomEventDataModel(
        @SerializedName("id") var uuid: String = "",
        @SerializedName("date") var date: String = "",
        @SerializedName("name") var name: String = ""
)