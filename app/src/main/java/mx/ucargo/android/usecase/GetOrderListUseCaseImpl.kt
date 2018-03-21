package mx.ucargo.android.usecase

import mx.ucargo.android.data.OrderRepository
import mx.ucargo.android.entity.Order


class GetOrderListUseCaseImpl(private val orderRepository: OrderRepository) : GetOrderListUseCase {
    override fun execute(success: (List<Order>) -> Unit, failure: (Throwable) -> Unit) {
        orderRepository.getOrderList(success, failure)
    }
}
