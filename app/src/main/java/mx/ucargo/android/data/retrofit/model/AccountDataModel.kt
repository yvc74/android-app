package mx.ucargo.android.data.retrofit.model

import com.google.gson.annotations.SerializedName

data class AccountDataModel(
        @SerializedName("name") var name: String = "",
        @SerializedName("email") var email: String = "",
        @SerializedName("picture") var picture: String = "",
        @SerializedName("token") var token: String = "",
        @SerializedName("driver_id") var driverid: String = "",
        @SerializedName("username") var username: String = "",
        @SerializedName("score") var score: Int = 0,
        @SerializedName("phone") var phone: String = ""
)
