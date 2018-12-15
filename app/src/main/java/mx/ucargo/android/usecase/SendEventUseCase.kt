package mx.ucargo.android.usecase

import mx.ucargo.android.reportlock.PictureUrlPayload
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
                Event.Collect -> sendCollect(orderId,eventPayload)
                Event.Begin -> beginOrder(orderId,eventPayload)
                Event.Green -> greenReport(orderId,eventPayload)
                Event.Red -> redReport(orderId,eventPayload)
                Event.ReportLock -> reportLock(orderId, eventPayload as PictureUrlPayload)
                Event.BeginRoute -> reportBeginRoute(orderId,eventPayload)
                Event.Store -> reportStore(orderId,eventPayload)
                Event.ReportLocation -> reportLocation(orderId, eventPayload as ReportLocationEventPayload)
                Event.ReportSign -> reportSign(orderId,eventPayload as PictureUrlPayload)
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
        order.status = Order.Status.OnRouteToCustom

        order = apiGateway.beginRouteToCustom(order)

        eventQueue.enqueue(order.id, Event.Begin, eventPayload)

        return order.status
    }


    private fun sendCollect(orderId: String, eventPayload: EventPayload): Order.Status {
        var order = apiGateway.findById(orderId)
        order.status = Order.Status.Collected

        order = apiGateway.reportCollect(order)

        eventQueue.enqueue(order.id, Event.Begin, eventPayload)

        return order.status
    }

    private fun greenReport(orderId: String, eventPayload: EventPayload): Order.Status{
        var order = apiGateway.findById(orderId)
        order.status = Order.Status.ReportedGreen

        order = apiGateway.reportCustom(order,"ReportGreen")

        eventQueue.enqueue(orderId,Event.Green,eventPayload)

        return  order.status
    }


    private fun redReport(orderId: String, eventPayload: EventPayload): Order.Status{
        var order = apiGateway.findById(orderId)
        order.status = Order.Status.ReportedRed

        order = apiGateway.reportCustom(order,"ReportRed")

        eventQueue.enqueue(orderId,Event.Green,eventPayload)

        return  order.status
    }


    private fun reportLock(orderId: String, eventPayload: PictureUrlPayload): Order.Status{
        var order = apiGateway.findById(orderId)
        order.status = Order.Status.ReportedLock

        order = apiGateway.reportLock(order,eventPayload.url)

        eventQueue.enqueue(orderId,Event.Green,eventPayload)

        return  order.status
    }

    private fun reportBeginRoute(orderId: String,eventPayload: EventPayload): Order.Status{
        var order = apiGateway.findById(orderId)
        order.status = Order.Status.OnRoute

        order = apiGateway.reportBeginRouten(order)

        eventQueue.enqueue(orderId,Event.Green,eventPayload)

        return  order.status
    }


    private fun reportStore(orderId: String, eventPayload: EventPayload): Order.Status {
        var order = apiGateway.findById(orderId)
        order.status = Order.Status.Stored

        order = apiGateway.reportStore(order)

        eventQueue.enqueue(orderId,Event.Green,eventPayload)

        return  order.status
    }

    private fun reportSign(orderId: String,eventPayload: PictureUrlPayload): Order.Status{
        var order = apiGateway.findById(orderId)
        order.status = Order.Status.ReportedSign

        order = apiGateway.reportSign(order, eventPayload.url)

        eventQueue.enqueue(orderId,Event.Green,eventPayload)

        return  order.status
    }

    private fun reportLocation(orderId: String, eventPayload: ReportLocationEventPayload): Order.Status{
        var order = apiGateway.findById(orderId)
        order = apiGateway.reportLocation(order,eventPayload.mcurrentLocation)
        eventQueue.enqueue(orderId,Event.Green,eventPayload)

        return  order.status

    }
}
