package mx.ucargo.android.sendquote

import mx.ucargo.android.entity.Order
import mx.ucargo.android.orderdetails.OrderDetailsModel

object Mappers {
    fun mapOrderDetailsModelStatus(status: Order.Status) = when (status) {
        Order.Status.NEW -> OrderDetailsModel.Status.NEW
        Order.Status.SENT_QUOTE -> OrderDetailsModel.Status.SENT_QUOTE
    }
}
