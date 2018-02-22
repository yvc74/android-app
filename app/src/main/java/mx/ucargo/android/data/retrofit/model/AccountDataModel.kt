package mx.ucargo.android.data.retrofit.model

import com.google.gson.annotations.SerializedName

data class AccountDataModel(
        @SerializedName("name") var name: String = "",
        @SerializedName("email") var email: String = "",
        @SerializedName("picture") var picture: String = "",
        @SerializedName("token") var token: String = ""
)
