package mx.ucargo.android.orderdetails

data class OrderDetailsModel(
        var originName: String = "",
        var originLatLng: Pair<Double, Double> = Pair(0.0, 0.0),
        var destinationName: String = "",
        var destinationLatLng: Pair<Double, Double> = Pair(0.0, 0.0),
        var orderType: String = "",
        var remainingTime: String = "",
        var pickUpAddress: String = "",
        var deliverAddress: String = "",
        var details: List<OrderDetailModel> = emptyList()
)
