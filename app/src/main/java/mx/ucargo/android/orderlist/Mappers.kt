package mx.ucargo.android.orderlist

import mx.ucargo.android.entity.Order
import mx.ucargo.android.orderdetails.OrderDetailModel
import mx.ucargo.android.orderdetails.OrderDetailsModel

object Mappers {
    fun mapOrderDetailsModel(order: Order) = OrderDetailsModel(
            originName = order.origin.name,
            originLatLng = Pair(order.origin.latitude, order.origin.longitude),
            pickUpAddress = order.origin.address,
            destinationName = order.destination.name,
            destinationLatLng = Pair(order.destination.latitude, order.destination.longitude),
            deliverAddress = order.destination.address,
            orderType = order.type,
            details = order.details.map { mapOrderDetailModel(it) }
    )

    private fun mapOrderDetailModel(detail: Order.Detail) =
            OrderDetailModel(icon = detail.icon, label = detail.label, value = detail.value)
}
