package mx.ucargo.android.orderdetails

import mx.ucargo.android.entity.Order

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

    private fun mapOrderDetailModel(detail: Order.Detail) = OrderDetailModel(name = detail.name, value = detail.value)
}
