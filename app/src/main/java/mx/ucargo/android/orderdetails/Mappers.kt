package mx.ucargo.android.orderdetails

import mx.ucargo.android.data.retrofit.model.OrderDataModel
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

    private fun mapOrderDetailPickup(detail: Location) = OrderDetailsPickUpModel(address =detail.address, date = "2018-19-09",attendant = "",label = detail.label )

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
        Order.Status.Customs -> OrderDetailsModel.Status.CUSTOMS
        Order.Status.Red -> OrderDetailsModel.Status.RED
        Order.Status.OnRoute -> OrderDetailsModel.Status.ONROUTE
        Order.Status.Finished -> OrderDetailsModel.Status.FINISHED
        Order.Status.OnRouteToCustom -> OrderDetailsModel.Status.ONROUTETOCUSTOM
        Order.Status.ReportedGreen -> OrderDetailsModel.Status.REPORTEDGREEN
        Order.Status.ReportedRed -> OrderDetailsModel.Status.REPORTEDRED
        Order.Status.ReportedLock -> OrderDetailsModel.Status.REPORTEDLOCK
        Order.Status.OnTracking -> OrderDetailsModel.Status.ONTRACKING
        Order.Status.ReportedSign -> OrderDetailsModel.Status.REPORTEDSIGN
        Order.Status.Stored -> OrderDetailsModel.Status.STORED
        Order.Status.Signed -> OrderDetailsModel.Status.REPORTEDSIGN
        Order.Status.BeginRoute -> OrderDetailsModel.Status.BEGINROUTE
        Order.Status.Collected -> OrderDetailsModel.Status.COLLECTED
    }
}
