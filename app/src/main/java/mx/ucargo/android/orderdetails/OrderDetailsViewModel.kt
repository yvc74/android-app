package mx.ucargo.android.orderdetails

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import mx.ucargo.android.orderdetails.Mappers.mapOrderDetailsModel
import mx.ucargo.android.usecase.GetOrderUseCase

class OrderDetailsViewModel(private val getOrderUseCase: GetOrderUseCase) : ViewModel() {
    val order = MutableLiveData<OrderDetailsModel>()
    val error = MutableLiveData<Throwable>()

    init {
    }

    fun getOrder(orderId: String) {
        getOrderUseCase.execute(orderId, {
            order.postValue(mapOrderDetailsModel(it))
        }, {
            error.postValue(it)
        })
    }

    class Factory(private val getOrderUseCase: GetOrderUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return OrderDetailsViewModel(getOrderUseCase) as T
        }

    }
}
