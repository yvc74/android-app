package mx.ucargo.android.data

import mx.ucargo.android.entity.Order

interface OrderRepository {
    fun findById(orderId: String, success: (Order) -> Unit, failure: (Throwable) -> Unit)
    fun persistQuote(quote: Int, orderId: String, success: () -> Unit, failure: (Throwable) -> Unit)
}
