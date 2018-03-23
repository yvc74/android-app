package mx.ucargo.android.data.retrofit.model

import com.google.gson.annotations.SerializedName

class OrderDataModel {
    @SerializedName("order_number")
    var orderNumber = ""

    @SerializedName("pickup")
    var pickup = LocationDataModel()

    @SerializedName("delivery")
    var delivery = LocationDataModel()

    @SerializedName("type")
    var type = 1

    @SerializedName("deadline")
    var deadline = ""

    @SerializedName("details")
    var details: List<OrderDetailDataModel> = emptyList()

    @SerializedName("score")
    var score = ""

    @SerializedName("favorite")
    var favorite = false
}
