package mx.ucargo.android.begin

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import mx.ucargo.android.orderdetails.Mappers.mapOrderDetailsModelStatus
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.usecase.BeginUseCase

class BeginViewModel(private val beginUseCase: BeginUseCase) : ViewModel() {
    val orderStatus = MutableLiveData<OrderDetailsModel.Status>()
    val error = MutableLiveData<Throwable>()

    fun begin(orderId: String) {
        beginUseCase.execute(orderId, {
            orderStatus.postValue(mapOrderDetailsModelStatus(it))
        }, {
            error.postValue(it)
        })
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val beginUseCase: BeginUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return BeginViewModel(beginUseCase) as T
        }

    }
}
