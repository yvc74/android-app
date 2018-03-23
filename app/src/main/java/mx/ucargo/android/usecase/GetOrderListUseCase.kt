package mx.ucargo.android.usecase

import mx.ucargo.android.entity.Order


interface GetOrderListUseCase {
    fun execute(success: (List<Order>) -> Unit, failure: (Throwable) -> Unit)
}