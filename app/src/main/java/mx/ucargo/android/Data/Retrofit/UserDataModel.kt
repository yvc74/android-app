package mx.ucargo.android.Data.Retrofit

import com.google.gson.annotations.SerializedName

/**
 * Created by noeperezchamorro on 26/01/18.
 */
class UserDataModel {
    @SerializedName("username")
    var username = ""
    @SerializedName("password")
    var password = ""
    constructor(username: String,password: String){
        this.username = username
        this.password = password
    }
}