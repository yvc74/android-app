package mx.ucargo.android.usecase

import mx.ucargo.android.entity.Order

interface BeginUseCase {
    fun execute(orderId: String, success: (Order.Status) -> Unit, failure: (Throwable) -> Unit)
}
