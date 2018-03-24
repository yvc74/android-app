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
            detailsformat = formatdetails(order.details),
            details = order.details.map { mapOrderDetailModel(it) }
    )


    private fun formatdetails(details: List<Order.Detail>): String  {
        var formatString = StringBuilder()
        var orderString = arrayOfNulls<String>(3)
        for(detail: Order.Detail in details){
            when(detail.label){
                "transport"->orderString[0] = detail.value+"/"
                "weight"->orderString[1] = detail.value+"/"
                "merchandise_type"->orderString[2] = detail.value
            }
        }
        for (value in orderString) formatString.append(value)
        return formatString.toString()
    }

    private fun mapOrderDetailModel(detail: Order.Detail) =
            OrderDetailModel(icon = detail.icon, label = detail.label, value = detail.value)
}
