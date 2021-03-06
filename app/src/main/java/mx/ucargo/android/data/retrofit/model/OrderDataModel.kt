package mx.ucargo.android.data.retrofit.model

import com.google.gson.annotations.SerializedName

data class OrderDataModel(
        @SerializedName("id") var orderNumber: String = "",
        @SerializedName("status") var status: String = "",
        @SerializedName("pickup") var pickup: LocationDataModel = LocationDataModel(),
        @SerializedName("delivery") var delivery: LocationDataModel = LocationDataModel(),
        @SerializedName("custom") var customs: LocationDataModel = LocationDataModel(),
        @SerializedName("type") var type: Int = 1,
        @SerializedName("deadline") var deadline: String = "",
        @SerializedName("details") var details: List<OrderDetailDataModel> = emptyList(),
        @SerializedName("score") var score: String = "",
        @SerializedName("favorite") var favorite: Boolean = false,
        @SerializedName("quoted_price") var quote: String = ""
)
