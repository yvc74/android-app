package mx.ucargo.android.usecase

import mx.ucargo.android.entity.Order

interface SendQuoteUseCase {
    fun execute(quote: Int, orderId: String, success: (Order.Status) -> Unit, failure: (Throwable) -> Unit)
}
