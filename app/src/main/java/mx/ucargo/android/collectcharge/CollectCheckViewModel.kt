package mx.ucargo.android.collectcharge

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import mx.ucargo.android.entity.EmptyEventPayload
import mx.ucargo.android.entity.Event
import mx.ucargo.android.orderdetails.Mappers
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.usecase.SendEventUseCase


class CollectCheckViewModel (private val sendEventUseCase: SendEventUseCase) : ViewModel() {
    val orderStatus = MutableLiveData<OrderDetailsModel.Status>()
    val error = MutableLiveData<Throwable>()

    fun collect(orderId: String) {
        sendEventUseCase.execute(orderId, Event.Collect, EmptyEventPayload(), {
            orderStatus.postValue(Mappers.mapOrderDetailsModelStatus(it))
        }, {
            error.postValue(it)
        })
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val sendEventUseCase: SendEventUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CollectCheckViewModel(sendEventUseCase) as T
        }
    }
}