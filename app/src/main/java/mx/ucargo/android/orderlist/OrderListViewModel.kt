package mx.ucargo.android.orderlist

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.orderlist.Mappers.mapOrderDetailsModel
import mx.ucargo.android.usecase.GetOrderListUseCase
import javax.inject.Inject

class OrderListViewModel @Inject constructor(private val getOrderListUseCase: GetOrderListUseCase) : ViewModel() {
    val orderList = MutableLiveData<List<OrderDetailsModel>>()
    val error = MutableLiveData<Throwable>()
    val loading = MutableLiveData<Boolean>()

    init {
        getOrderList()
    }

    fun getOrderList() {
        loading.postValue(true)
        getOrderListUseCase.execute({
            orderList.postValue(it.map { mapOrderDetailsModel(it) })
            loading.postValue(false)
        }, {
            error.postValue(it)
            loading.postValue(false)
        })
    }
}
