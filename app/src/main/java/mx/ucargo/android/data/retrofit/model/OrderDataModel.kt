package mx.ucargo.android.data.retrofit.model

import com.google.gson.annotations.SerializedName

class OrderDataModel {
    @SerializedName("order_number")
    var orderNumber = ""

    @SerializedName("origin")
    var origin = LocationDataModel()

    @SerializedName("destination")
    var destination = LocationDataModel()

    @SerializedName("type")
    var type = 1

    @SerializedName("pick_up_address")
    var pickUpAddress = ""

    @SerializedName("deadline")
    var deadline = ""

    @SerializedName("details")
    var details: List<OrderDetailDataModel> = emptyList()
}
