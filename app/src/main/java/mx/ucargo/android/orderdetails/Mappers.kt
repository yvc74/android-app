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
