package mx.ucargo.android.orderdetails

import mx.ucargo.android.entity.Location
import mx.ucargo.android.entity.Order
import java.util.*

object Mappers {
    fun mapOrderDetailsModel(order: Order, referenceDate: Date) = OrderDetailsModel(
            id = order.id,
            originName = order.customs.name,
            originLatLng = Pair(order.customs.latitude, order.customs.longitude),

            pickUpLatLng = Pair(order.pickup.latitude,order.pickup.longitude),
            pickUpAddress = order.pickup.address,

            destinationName = order.delivery.name,
            destinationLatLng = Pair(order.delivery.latitude, order.delivery.longitude),

            deliverAddress = order.delivery.address,
            orderType = order.type,
            details = order.details.map { mapOrderDetailModel(it) },
            detailsformat = formatdetails(order.details),
            deliveryDetails=mapOrderDetailDelivery(order.delivery,order.pickup,order.customs,order.type),
            remainingTime = daysHoursDiff(referenceDate, order.quoteDeadline),
            quote = order.quote,
            status = mapOrderDetailsModelStatus(order.status)
            //status = mapOrderDetailsModelStatus(Order.Status.APPROVED)
    )

    private fun formatdetails(details: List<Order.Detail>): String {
        val formatString = StringBuilder();
        details.forEach {
            formatString
                    .append(it.label)
                    .append(":")
                    .append(it.value)
                    .append("/")
        }
        return formatString.toString()
    }

    private fun mapOrderDetailDelivery(delivery: Location, pickup: Location,customs : Location,type : Order.Type ):List<OrderDetailsPickUpModel>{
        when(type){
            Order.Type.IMPORT-> return listOf<OrderDetailsPickUpModel>(mapOrderDetailPickup(customs),mapOrderDetailPickup(delivery))
            Order.Type.EXPORT -> return listOf<OrderDetailsPickUpModel>(mapOrderDetailPickup(delivery),mapOrderDetailPickup(pickup),mapOrderDetailPickup(customs))
            else -> return emptyList()
        }
    }

    private fun mapOrderDetailPickup(detail: Location) = OrderDetailsPickUpModel(address =detail.address, date = detail.schedule.split(" |\\t".toRegex())[0],hour = detail.schedule.split(" |\\t".toRegex())[1]+" hrs",attendant = "",label = detail.label )

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
        Order.Status.New -> OrderDetailsModel.Status.NEW
        Order.Status.Quoted -> OrderDetailsModel.Status.SENT_QUOTE
        Order.Status.Approved -> OrderDetailsModel.Status.APPROVED
        Order.Status.CUSTOMS -> OrderDetailsModel.Status.CUSTOMS
        Order.Status.RED -> OrderDetailsModel.Status.RED
        Order.Status.ONROUTE -> OrderDetailsModel.Status.ONROUTE
        Order.Status.FINISHED -> OrderDetailsModel.Status.FINISHED
        Order.Status.OnRouteToCustom -> OrderDetailsModel.Status.ONROUTETOCUSTOM
        Order.Status.ReportedGreen -> OrderDetailsModel.Status.REPORTEDGREEN
        Order.Status.ReportedLock -> OrderDetailsModel.Status.REPORTEDLOCK
        Order.Status.OnTracking -> OrderDetailsModel.Status.ONTRACKING
    }
}
