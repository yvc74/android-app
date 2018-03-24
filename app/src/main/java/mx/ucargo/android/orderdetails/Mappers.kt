package mx.ucargo.android.orderdetails

import mx.ucargo.android.entity.Order
import java.util.*

object Mappers {
    fun mapOrderDetailsModel(order: Order, referenceDate: Date) = OrderDetailsModel(
            originName = order.pickup.name,
            originLatLng = Pair(order.pickup.latitude, order.pickup.longitude),
            pickUpAddress = order.pickup.address,
            destinationName = order.delivery.name,
            destinationLatLng = Pair(order.delivery.latitude, order.delivery.longitude),
            deliverAddress = order.delivery.address,
            orderType = order.type,
            details = order.details.map { mapOrderDetailModel(it) },
            detailsformat = formatdetails(order.details),
            remainingTime = daysHoursDiff(referenceDate, order.quoteDeadline),
            quote = 2000,
            status = mapOrderDetailsModelStatus(order.status)
    )

    private fun formatdetails(details: List<Order.Detail>): String  {
        var formatString = StringBuilder();
        for(detail: Order.Detail in details){
            formatString.append(detail.label+":"+detail.value+"/")
        }
        return formatString.toString()
    }

    private fun mapOrderDetailModel(detail: Order.Detail) = OrderDetailModel(icon = detail.icon, label = detail.label, value = detail.value)

    private fun hoursDiff(start: Date, end: Date): Int {
        var hours = 0
        val counterCalendar = Calendar.getInstance()
        var endCalendar = Calendar.getInstance()

        counterCalendar.time = start
        endCalendar.time = end

        while (counterCalendar.before(endCalendar)) {
            counterCalendar.add(Calendar.HOUR, 1)
            hours++
        }

        return hours
    }

    fun daysHoursDiff(start: Date, end: Date): Pair<Int, Int> {
        val hours = hoursDiff(start, end)
        val days = hours / 24

        return Pair(days, hours % 24)
    }

    fun mapOrderDetailsModelStatus(status: Order.Status) = when (status) {
        Order.Status.NEW -> OrderDetailsModel.Status.NEW
        Order.Status.SENT_QUOTE -> OrderDetailsModel.Status.SENT_QUOTE
        Order.Status.APPROVED -> OrderDetailsModel.Status.APPROVED
        Order.Status.CUSTOMS -> OrderDetailsModel.Status.CUSTOMS
    }
}
