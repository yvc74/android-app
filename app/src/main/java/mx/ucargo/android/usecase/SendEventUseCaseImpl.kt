package mx.ucargo.android.usecase

import mx.ucargo.android.data.OrderRepository
import mx.ucargo.android.entity.*

class SendEventUseCaseImpl(private val orderRepository: OrderRepository) : SendEventUseCase {
    override fun execute(orderId: String, event: Event, eventPayload: EventPayload, success: (Order.Status) -> Unit, failure: (Throwable) -> Unit) {
        when (event) {
            Event.Quote -> sendQuote(orderId, eventPayload, success, failure)
            Event.Begin -> success.invoke(Order.Status.CUSTOMS)
        }
    }

    private fun sendQuote(orderId: String, eventPayload: EventPayload, success: (Order.Status) -> Unit, failure: (Throwable) -> Unit) {
        if (eventPayload !is QuoteEventPayload) {
            failure.invoke(IllegalArgumentException())
            return
        }

        if (eventPayload.quote == 0) {
            failure.invoke(EmptyQuote())
            return
        }

        orderRepository.persistQuote(eventPayload.quote, orderId, {
            success.invoke(Order.Status.SENT_QUOTE)
        }, failure)
    }
}
