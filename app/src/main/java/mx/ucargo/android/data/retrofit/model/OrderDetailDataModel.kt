package mx.ucargo.android.data.retrofit.model

import com.google.gson.annotations.SerializedName

class OrderDetailDataModel {
    @SerializedName("label")
    var label = ""

    @SerializedName("url")
    var url = ""

    @SerializedName("value")
    var value = ""
}
