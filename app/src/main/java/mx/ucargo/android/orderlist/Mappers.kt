package mx.ucargo.android.orderlist

import mx.ucargo.android.entity.Order
import mx.ucargo.android.orderdetails.OrderDetailModel
import mx.ucargo.android.orderdetails.OrderDetailsModel

object Mappers {
    fun mapOrderDetailsModel(order: Order) = OrderDetailsModel(
            id = order.id,
            originName = order.customs.name,
            originLatLng = Pair(order.customs.latitude, order.customs.longitude),
            pickUpAddress = order.pickup.address,
            destinationName = order.delivery.name,
            destinationLatLng = Pair(order.delivery.latitude, order.delivery.longitude),
            deliverAddress = order.delivery.address,
            orderType = order.type,
            detailsformat = formatDetails(order.details),
            details = order.details.map { mapOrderDetailModel(it) },
            status = mapOrderDetailsModelStatus(order.status)
    )


    private fun formatDetails(details: List<Order.Detail>) = details.filter {
        when (it.label) {
            "transport" -> true
            "weight" -> true
            "merchandise_type" -> true
            else -> false
        }
    }.joinToString(separator = "/") { it.value }

    private fun mapOrderDetailModel(detail: Order.Detail) =
            OrderDetailModel(icon = detail.icon, label = detail.label, value = detail.value)

    fun mapOrderDetailsModelStatus(status: Order.Status) = when (status) {
        Order.Status.New -> OrderDetailsModel.Status.NEW
        Order.Status.Quoted -> OrderDetailsModel.Status.SENT_QUOTE
        Order.Status.Approved -> OrderDetailsModel.Status.APPROVED
        Order.Status.Customs -> OrderDetailsModel.Status.CUSTOMS
        Order.Status.Red -> OrderDetailsModel.Status.RED
        Order.Status.OnRoute -> OrderDetailsModel.Status.ONROUTE
        Order.Status.Finished -> OrderDetailsModel.Status.FINISHED
        Order.Status.OnRouteToCustom -> OrderDetailsModel.Status.ONROUTETOCUSTOM
        Order.Status.ReportedGreen -> OrderDetailsModel.Status.REPORTEDGREEN
        Order.Status.ReportedLock -> OrderDetailsModel.Status.REPORTEDLOCK
        Order.Status.OnTracking -> OrderDetailsModel.Status.ONTRACKING
        Order.Status.ReportedSign -> OrderDetailsModel.Status.REPORTEDSIGN
        Order.Status.Stored -> OrderDetailsModel.Status.STORED
        Order.Status.Signed -> OrderDetailsModel.Status.REPORTEDSIGN
        Order.Status.BeginRoute -> OrderDetailsModel.Status.BEGINROUTE
        Order.Status.Collected -> OrderDetailsModel.Status.COLLECTED
        Order.Status.ReportedRed -> OrderDetailsModel.Status.REPORTEDRED
    }
}
