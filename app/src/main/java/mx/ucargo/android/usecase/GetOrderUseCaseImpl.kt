package mx.ucargo.android.usecase

import mx.ucargo.android.data.OrderRepository
import mx.ucargo.android.entity.Order

class GetOrderUseCaseImpl(private val orderRepository: OrderRepository) : GetOrderUseCase {
    override fun execute(orderId: String, success: (Order) -> Unit, failure: (Throwable) -> Unit) {
        orderRepository.findById(orderId, success, failure)
    }
}
