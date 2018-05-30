package mx.ucargo.android.usecase

import mx.ucargo.android.data.ApiGateway
import mx.ucargo.android.data.DatabaseGateway
import mx.ucargo.android.entity.*
import java.util.*
import kotlin.concurrent.thread

interface SendEventUseCase {
    fun execute(orderId: String, event: Event, eventPayload: EventPayload, success: (Order.Status) -> Unit, failure: ((Throwable) -> Unit)? = null)
}

class SendEventUseCaseImpl(private val apiGateway: ApiGateway,
                           private val databaseGateway: DatabaseGateway) : SendEventUseCase {
    override fun execute(orderId: String, event: Event, eventPayload: EventPayload, success: (Order.Status) -> Unit, failure: ((Throwable) -> Unit)?) {
        thread {
            try {
                success.invoke(executeSync(orderId, event, eventPayload))
            } catch (t: Throwable) {
                failure?.invoke(t)
            }
        }
    }

    internal fun executeSync(orderId: String, event: Event, eventPayload: EventPayload) =
            when (event) {
                Event.Quote -> enqueueQuote(orderId, eventPayload)
                Event.Begin -> Order.Status.CUSTOMS
                else -> throw Exception("Unknown event")
            }


    private fun enqueueQuote(orderId: String, eventPayload: EventPayload): Order.Status {
        if (eventPayload !is QuoteEventPayload) throw IllegalArgumentException()
        if (eventPayload.quote == 0) throw EmptyQuoteException()

        val order = try {
            apiGateway.findById(orderId)
        } catch (e: Exception) {
            databaseGateway.findOrderById(orderId)
        } ?: throw Exception("Order not found")

        order.quote = eventPayload.quote
        order.status = Order.Status.SENT_QUOTE

        order.id = databaseGateway.createOrUpdateOrder(order)

        databaseGateway.enqueueEvent(QueuedEvent(
                uuid = UUID.randomUUID().toString(),
                event = Event.Quote,
                status = QueuedEvent.Status.PENDING
        ))

        return order.status
    }
}
