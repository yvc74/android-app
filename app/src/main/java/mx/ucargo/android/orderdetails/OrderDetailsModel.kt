package mx.ucargo.android.orderdetails

data class OrderDetailsModel(
        var originName: String = "",
        var destinationName: String = "",
        var orderType: String = "",
        var remainingTime: String = "",
        var pickUpAddress: String = "",
        var deliverAddress: String = "",
        var details : List<OrderDetailModel> = emptyList()
)
