package mx.ucargo.android.data.retrofit

import com.google.gson.annotations.SerializedName

class UserDataModel {
    @SerializedName("username")
    var username = ""
    @SerializedName("password")
    var password = ""

    constructor(username: String, password: String) {
        this.username = username
        this.password = password
    }
}
