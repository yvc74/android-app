package mx.ucargo.android.data.retrofit

import com.google.gson.annotations.SerializedName

class AccountDataModel {
    @SerializedName("name")
    var name = ""

    @SerializedName("email")
    var email = ""

    @SerializedName("picture")
    var picture = ""

    @SerializedName("token")
    var token = ""
}
