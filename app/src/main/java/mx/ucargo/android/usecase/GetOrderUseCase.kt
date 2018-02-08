package mx.ucargo.android.usecase

import mx.ucargo.android.entity.Order

interface GetOrderUseCase {
    fun execute(orderId: String, success: (Order) -> Unit, failure: (Throwable) -> Unit)
}
