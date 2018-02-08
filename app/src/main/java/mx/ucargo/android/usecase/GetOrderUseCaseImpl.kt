package mx.ucargo.android.usecase

import mx.ucargo.android.entity.Order

class GetOrderUseCaseImpl : GetOrderUseCase {
    override fun execute(orderId: String, success: (Order) -> Unit, failure: (Throwable) -> Unit) {

    }
}
