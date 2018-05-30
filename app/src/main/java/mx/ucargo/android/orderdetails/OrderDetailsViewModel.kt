package mx.ucargo.android.orderdetails

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import mx.ucargo.android.orderdetails.Mappers.mapOrderDetailsModel
import mx.ucargo.android.usecase.GetOrderUseCase
import java.util.*
import javax.inject.Inject

interface DateProvider : () -> Date

class OrderDetailsViewModel @Inject constructor(private val getOrderUseCase: GetOrderUseCase,
                                                private val reference: DateProvider) : ViewModel() {
    val order = MutableLiveData<OrderDetailsModel>()
    val error = MutableLiveData<Throwable>()

    fun getOrder(orderId: String) {
        getOrderUseCase.execute(orderId, {
            order.postValue(mapOrderDetailsModel(it, reference.invoke()))
        }, {
            error.postValue(it)
        })
    }
}
