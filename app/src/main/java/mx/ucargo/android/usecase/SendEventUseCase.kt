package mx.ucargo.android.usecase

import mx.ucargo.android.data.ApiGateway
import mx.ucargo.android.data.EventQueue
import mx.ucargo.android.entity.*
import kotlin.concurrent.thread

interface SendEventUseCase {
    fun execute(orderId: String, event: Event, eventPayload: EventPayload, success: (Order.Status) -> Unit, failure: ((Throwable) -> Unit)? = null)
}

class SendEventUseCaseImpl(private val apiGateway: ApiGateway,
                           private val eventQueue: EventQueue) : SendEventUseCase {
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
                Event.Quote -> sendQuote(orderId,eventPayload)
                Event.Begin -> beginOrder(orderId,eventPayload)
                Event.Green -> greenReport(orderId,eventPayload)
                else -> throw Exception("Unknown event")
            }


    private fun sendQuote(orderId: String, eventPayload: EventPayload): Order.Status {
        if (eventPayload !is QuoteEventPayload) throw IllegalArgumentException()
        if (eventPayload.quote == 0) throw EmptyQuote()

        var order = apiGateway.findById(orderId)
        order.status = Order.Status.Quoted
        order.quote = eventPayload.quote

        order = apiGateway.sendQuote(order)

        eventQueue.enqueue(order.id, Event.Quote, eventPayload)

        return order.status
    }

    private fun beginOrder(orderId: String, eventPayload: EventPayload): Order.Status {
        var order = apiGateway.findById(orderId)
        order.status = Order.Status.ONROUTE

        order = apiGateway.beginRouteToCustom(order)

        eventQueue.enqueue(order.id, Event.Begin, eventPayload)

        return order.status
    }


    private fun greenReport(orderId: String, eventPayload: EventPayload): Order.Status{
        var order = apiGateway.findById(orderId)
        order.status = Order.Status.ReportedGreen

        order = apiGateway.reportGreen(order,"ReportGreen")

        eventQueue.enqueue(orderId,Event.Green,eventPayload)

        return  order.status
    }
}
