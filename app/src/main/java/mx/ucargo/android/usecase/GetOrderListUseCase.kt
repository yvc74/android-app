package mx.ucargo.android.usecase

import kotlinx.android.synthetic.main.order_list_activity.*
import mx.ucargo.android.app.setUpDrawer
import mx.ucargo.android.data.ApiGateway
import mx.ucargo.android.entity.Order
import kotlin.concurrent.thread


interface GetOrderListUseCase {
    fun execute(typeOrderList: Int,success: (List<Order>) -> Unit, failure: ((Throwable) -> Unit)? = null)
}

class GetOrderListUseCaseImpl(private val apiGateway: ApiGateway) : GetOrderListUseCase {

    override fun execute(typeOrderList: Int,success: (List<Order>) -> Unit, failure: ((Throwable) -> Unit)?) {
        thread {
            try {
                success.invoke(executeSync(typeOrderList))
            } catch (t: Throwable) {
                failure?.invoke(t)
            }
        }
    }

    internal fun executeSync(typeOrderList: Int) =
        when (typeOrderList) {
            0 -> apiGateway.getOrderList()
            1 ->apiGateway.getOrderAssigned()
            2 -> apiGateway.getOrderLog()
            else -> apiGateway.getOrderList()
        }

}
