package mx.ucargo.android.usecase

import mx.ucargo.android.entity.Order

class BeginUseCaseImpl : BeginUseCase {
    override fun execute(orderId: String, success: (Order.Status) -> Unit, failure: (Throwable) -> Unit) {
        success.invoke(Order.Status.CUSTOMS)
    }
}
