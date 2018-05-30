package mx.ucargo.android.data

import mx.ucargo.android.entity.Order
import mx.ucargo.android.entity.QueuedEvent

interface DatabaseGateway {
    fun subscribeToNewEvents(onNewEvent: (QueuedEvent) -> Unit)

    fun findOrderById(orderId: String): Order?
    fun updateOrder(order: Order)
    fun createOrder(order: Order): String
    fun createOrUpdateOrder(order: Order): String

    fun enqueueEvent(queuedEvent: QueuedEvent)
}
