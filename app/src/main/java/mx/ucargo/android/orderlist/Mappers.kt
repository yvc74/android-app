package mx.ucargo.android.orderlist

import mx.ucargo.android.entity.Order
import mx.ucargo.android.orderdetails.OrderDetailModel
import mx.ucargo.android.orderdetails.OrderDetailsModel

object Mappers {
    fun mapOrderDetailsModel(order: Order) = OrderDetailsModel(
            originName = order.pickup.name,
            originLatLng = Pair(order.pickup.latitude, order.pickup.longitude),
            pickUpAddress = order.pickup.address,
            destinationName = order.delivery.name,
            destinationLatLng = Pair(order.delivery.latitude, order.delivery.longitude),
            deliverAddress = order.delivery.address,
            orderType = order.type,
            details = order.details.map { mapOrderDetailModel(it) }
    )

    private fun mapOrderDetailModel(detail: Order.Detail) =
            OrderDetailModel(icon = detail.icon, label = detail.label, value = detail.value)
}
