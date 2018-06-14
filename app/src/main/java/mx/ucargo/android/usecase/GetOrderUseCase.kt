package mx.ucargo.android.usecase

import mx.ucargo.android.data.ApiGateway
import mx.ucargo.android.data.DatabaseGateway
import mx.ucargo.android.entity.Order
import kotlin.concurrent.thread

interface GetOrderUseCase {
    fun execute(orderId: String, success: (Order) -> Unit, failure: ((Throwable) -> Unit)? = null)
}

class GetOrderUseCaseImpl(private val apiGateway: ApiGateway,
                          private val databaseGateway: DatabaseGateway) : GetOrderUseCase {
    override fun execute(orderId: String, success: (Order) -> Unit, failure: ((Throwable) -> Unit)?) {
        thread {
            try {
                success.invoke(executeSync(orderId))
            } catch (t: Throwable) {
                failure?.invoke(t)
            }
        }
    }

    internal fun executeSync(orderId: String) =
            databaseGateway.findOrderById(orderId)
                    ?: apiGateway.findById(orderId)
                    ?: throw Exception("Order not found")
}
