package mx.ucargo.android.data.retrofit.model

import com.google.gson.annotations.SerializedName

data class SignEventDataModel(
        @SerializedName("id") var uuid: String = "",
        @SerializedName("name") var name: String = "",
        @SerializedName("date") var date: String = "",
        @SerializedName("picture") var picture: String = ""
)
