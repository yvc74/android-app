package mx.ucargo.android.data.retrofit.model

import com.google.gson.annotations.SerializedName

class OrderDataModel {
    @SerializedName("origin")
    var origin = LocationDataModel()

    @SerializedName("destination")
    var destination = LocationDataModel()

    @SerializedName("type")
    var type = 1

    @SerializedName("distance")
    var distance = ""

    @SerializedName("merchandise_type")
    var merchandiseType = ""

    @SerializedName("order_number")
    var orderNumber = ""

    @SerializedName("transport")
    var transport = ""

    @SerializedName("weight")
    var weight = ""

    @SerializedName("pick_up_address")
    var pickUpAddress = ""

    @SerializedName("deadline")
    var deadline = ""
}
