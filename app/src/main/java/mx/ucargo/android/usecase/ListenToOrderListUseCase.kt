package mx.ucargo.android.usecase

import mx.ucargo.android.data.ApiGateway
import mx.ucargo.android.data.DatabaseGateway
import mx.ucargo.android.entity.Order
import kotlin.concurrent.thread


interface ListenToOrderListUseCase {
    fun execute(listener: (List<Order>) -> Unit, failure: ((Throwable) -> Unit)? = null)
}

class ListenToOrderListUseCaseImpl(private val apiGateway: ApiGateway,
                                   private val databaseGateway: DatabaseGateway) : ListenToOrderListUseCase {
    override fun execute(listener: (List<Order>) -> Unit, failure: ((Throwable) -> Unit)?) {
        thread {
            try {
                databaseGateway.subscribeToOrders({
                    listener.invoke(it)
                })

                executeSync()
            } catch (t: Throwable) {
                failure?.invoke(t)
            }
        }
    }

    internal fun executeSync(): List<Order> {
        val orderList = apiGateway.getOrderList()
        
        orderList.forEach {
            databaseGateway.createOrUpdateOrder(it)
        }

        return orderList
    }
}
