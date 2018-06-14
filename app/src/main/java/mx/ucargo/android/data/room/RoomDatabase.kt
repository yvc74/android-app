package mx.ucargo.android.data.room

import io.reactivex.disposables.Disposable
import mx.ucargo.android.data.DatabaseGateway
import mx.ucargo.android.data.room.model.*
import mx.ucargo.android.entity.Event
import mx.ucargo.android.entity.Location
import mx.ucargo.android.entity.Order
import mx.ucargo.android.entity.QueuedEvent
import java.text.SimpleDateFormat
import java.util.*

class RoomDatabase(private val databaseDao: DatabaseDao) : DatabaseGateway {
    var observer: Disposable? = null
    var ordersObserver: Disposable? = null

    override fun subscribeToNewEvents(onNewEvent: (QueuedEvent) -> Unit) {
        observer?.dispose()

        observer = databaseDao.findQueuedEvent().subscribe {
            onNewEvent.invoke(it.toQueuedEvent())
        }
    }

    override fun enqueueEvent(queuedEvent: QueuedEvent) {
        databaseDao.insertEvent(queuedEvent.toEventDBModel())
    }


    override fun createOrUpdateOrder(order: Order): String {
        if (findOrderById(order.id) == null) {
            order.id = createOrder(order)
        } else {
            updateOrder(order)
        }

        return order.id
    }

    override fun subscribeToOrders(orders: (List<Order>) -> Unit) {
        ordersObserver?.dispose()

        ordersObserver = databaseDao.findOrders().subscribe {
            orders.invoke(it.map { it.toOrder() })
        }
    }

    override fun findOrderById(orderId: String): Order? {
        val order = databaseDao.findOrderById(orderId)?.toOrder()
        order?.details = databaseDao.findOrderDetailsByOrderId(orderId).map { it.toOrderDetail() }
        return order
    }

    override fun updateOrder(order: Order) {
        databaseDao.updateOrder(order.toOrderDBModel())
        databaseDao.deleteOrderDetails(databaseDao.findOrderDetailsByOrderId(order.id))
        order.details.forEach {
            databaseDao.insertOrderDetail(it.toOrderDetailDBModel(order.id))
        }
    }

    override fun createOrder(order: Order): String {
        databaseDao.insertOrder(order.toOrderDBModel())
        order.details.forEach {
            databaseDao.insertOrderDetail(it.toOrderDetailDBModel(order.id))
        }

        return order.id
    }
}

private fun Order.Detail.toOrderDetailDBModel(orderId: String) = OrderDetailDBModel(
        orderId = orderId,
        icon = icon,
        label = label,
        value = value
)

private fun QueuedEvent.toEventDBModel() = EventDBModel(
        status = when (status) {
            QueuedEvent.Status.PENDING -> STATUS_PENDING
            QueuedEvent.Status.SENT -> STATUS_SENT
        },
        event = when (event) {
            Event.Quote -> "Quote"
            Event.Begin -> "Begin"
            Event.Green -> "Green"
            Event.Red -> "Red"
        },
        uuid = uuid
)

private fun Order.toOrderDBModel() = OrderDBModel(
        id = id,
        pickup = pickup.toLocationDBModel(),
        delivery = delivery.toLocationDBModel(),
        type = when (type) {
            Order.Type.IMPORT -> ORDER_TYPE_IMPORT
            Order.Type.EXPORT -> ORDER_TYPE_EXPORT
        },
        quote = quote,
        quoteDeadline = quoteDeadline.toDBString(),
        status = when (status) {
            Order.Status.NEW -> ORDER_STATUS_NEW
            Order.Status.SENT_QUOTE -> ORDER_STATUS_SENT_QUOTE
            Order.Status.APPROVED -> ORDER_STATUS_APPROVED
            Order.Status.CUSTOMS -> ORDER_STATUS_CUSTOMS
        }
)

private fun Location.toLocationDBModel() = LocationDBModel(
        name = name,
        address = address,
        schedule = schedule,
        latitude = latitude,
        longitude = longitude
)

private fun OrderDetailDBModel.toOrderDetail() = Order.Detail(
        icon = icon,
        label = label,
        value = value
)

private fun OrderDBModel.toOrder() = Order(
        id = id,
        pickup = pickup.toLocation(),
        delivery = delivery.toLocation(),
        type = when (type) {
            ORDER_TYPE_IMPORT -> Order.Type.IMPORT
            else -> Order.Type.EXPORT
        },
        quote = quote,
        quoteDeadline = quoteDeadline.toDate(),
        status = when (status) {
            ORDER_STATUS_NEW -> Order.Status.NEW
            ORDER_STATUS_SENT_QUOTE -> Order.Status.SENT_QUOTE
            ORDER_STATUS_APPROVED -> Order.Status.APPROVED
            else -> Order.Status.CUSTOMS
        }
)

private fun LocationDBModel.toLocation() = Location(
        name = name,
        address = address,
        schedule = schedule,
        latitude = latitude,
        longitude = longitude
)

private fun EventDBModel.toQueuedEvent() = QueuedEvent()

private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSz")

private fun String.toDate() = dateFormat.parse(this)

private fun Date.toDBString() = dateFormat.format(this)
