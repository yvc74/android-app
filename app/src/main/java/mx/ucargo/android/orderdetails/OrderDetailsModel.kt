package mx.ucargo.android.orderdetails

import mx.ucargo.android.entity.Order

data class OrderDetailsModel(
        var id: String = "",
        var originName: String = "",
        var originLatLng: Pair<Double, Double> = Pair(0.0, 0.0),
        var destinationName: String = "",
        var destinationLatLng: Pair<Double, Double> = Pair(0.0, 0.0),
        var pickUpLatLng: Pair<Double, Double> = Pair(0.0, 0.0),
        var pickUpName: String = "",
        var orderType: Order.Type = Order.Type.IMPORT,
        var remainingTime: Pair<Int, Int> = Pair(0, 0),
        var pickUpAddress: String = "",
        var deliverAddress: String = "",
        var status: Status = Status.NEW,
        var quote: Int = 0,
        var deliveryDetails: List<OrderDetailsPickUpModel> = emptyList(),
        var details: List<OrderDetailModel> = emptyList(),
        var detailsformat: String = ""
) {
    enum class Status {
        NEW,
        SENT_QUOTE,
        APPROVED,
        CUSTOMS,
        REPORTEDGREEN,
        REPORTEDRED,
        REPORTEDLOCK,
        STORED,
        BEGINROUTE,
        RED,
        COLLECTED,
        ONROUTE,
        ONTRACKING,
        REPORTSIGN,
        REPORTCUSTOMEXPORT,
        ONROUTETOCUSTOM,
        REPORTEDSIGN,
        FINISHED
    }
}
