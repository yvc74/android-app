package mx.ucargo.android.usecase

import mx.ucargo.android.data.ApiGateway
import mx.ucargo.android.entity.Order
import kotlin.concurrent.thread


interface GetOrderListUseCase {
    fun execute(success: (List<Order>) -> Unit, failure: ((Throwable) -> Unit)? = null)
}

class GetOrderListUseCaseImpl(private val apiGateway: ApiGateway) : GetOrderListUseCase {
    override fun execute(success: (List<Order>) -> Unit, failure: ((Throwable) -> Unit)?) {
        thread {
            try {
                success.invoke(executeSync())
            } catch (t: Throwable) {
                failure?.invoke(t)
            }
        }
    }

    internal fun executeSync() = apiGateway.getOrderList()
}
