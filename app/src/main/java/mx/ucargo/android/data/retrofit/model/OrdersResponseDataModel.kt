package mx.ucargo.android.data.retrofit.model

import com.google.gson.annotations.SerializedName

data class OrdersResponseDataModel(
        @SerializedName("orders") var orders: List<OrderDataModel> = emptyList()
)
