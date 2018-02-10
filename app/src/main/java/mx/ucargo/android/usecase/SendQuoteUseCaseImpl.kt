package mx.ucargo.android.usecase

import mx.ucargo.android.data.OrderRepository
import mx.ucargo.android.entity.EmptyQuote
import mx.ucargo.android.entity.Order

class SendQuoteUseCaseImpl(private val orderRepository: OrderRepository) : SendQuoteUseCase {
    override fun execute(quote: Int, orderId: String, success: (Order.Status) -> Unit, failure: (Throwable) -> Unit) {
        if (quote == 0) {
            failure.invoke(EmptyQuote())
            return
        }

        orderRepository.persistQuote(quote, orderId, {
            success.invoke(Order.Status.SENT_QUOTE)
        }, failure)
    }
}
