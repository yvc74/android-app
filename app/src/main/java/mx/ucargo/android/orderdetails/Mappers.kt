package mx.ucargo.android.orderdetails

import mx.ucargo.android.entity.Order
import java.util.*

object Mappers {
    fun mapOrderDetailsModel(order: Order, referenceDate: Date) = OrderDetailsModel(
            originName = order.origin.name,
            originLatLng = Pair(order.origin.latitude, order.origin.longitude),
            pickUpAddress = order.origin.address,
            destinationName = order.destination.name,
            destinationLatLng = Pair(order.destination.latitude, order.destination.longitude),
            deliverAddress = order.destination.address,
            orderType = order.type,
            details = order.details.map { mapOrderDetailModel(it) },
            remainingTime = daysHoursDiff(referenceDate, order.quoteDeadline),
            quote = 2000
    )

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
}
