package mx.ucargo.android.orderdetails

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import mx.ucargo.android.orderdetails.Mappers.mapOrderDetailsModel
import mx.ucargo.android.usecase.GetOrderUseCase
import java.util.*

class OrderDetailsViewModel(private val getOrderUseCase: GetOrderUseCase,
                            private val reference: () -> Date) : ViewModel() {
    val order = MutableLiveData<OrderDetailsModel>()
    val error = MutableLiveData<Throwable>()

    fun getOrder(orderId: String) {
        getOrderUseCase.execute(orderId, {
            order.postValue(mapOrderDetailsModel(it, reference.invoke()))
        }, {
            error.postValue(it)
        })
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val getOrderUseCase: GetOrderUseCase,
                  private val reference: () -> Date) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return OrderDetailsViewModel(getOrderUseCase, reference) as T
        }
    }
}
