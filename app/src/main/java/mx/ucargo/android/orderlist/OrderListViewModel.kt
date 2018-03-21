package mx.ucargo.android.orderlist


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.orderlist.Mappers.mapOrderDetailsModel
import mx.ucargo.android.usecase.GetOrderListUseCase


class OrderListViewModel(val getOrderListUseCase: GetOrderListUseCase) : ViewModel() {
    val orderList = MutableLiveData<List<OrderDetailsModel>>()
    val error = MutableLiveData<Throwable>()
    val updating = MutableLiveData<Boolean>()

    fun getOrderList() {
        updating.postValue(true)
        getOrderListUseCase.execute({
            this.orderList.postValue(it.map { mapOrderDetailsModel(it) })
        }, {
            error.postValue(it)
        })
        updating.postValue(false)
    }


    class Factory(private val getOrderListUseCase: GetOrderListUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return OrderListViewModel(getOrderListUseCase) as T
        }

    }
}
