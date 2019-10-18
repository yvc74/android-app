package mx.ucargo.android.begin

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import mx.ucargo.android.entity.EmptyEventPayload
import mx.ucargo.android.entity.Event
import mx.ucargo.android.orderdetails.Mappers.mapOrderDetailsModelStatus
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.usecase.SendEventUseCase

class BeginViewModel(private val sendEventUseCase: SendEventUseCase) : ViewModel() {
    val orderStatus = MutableLiveData<OrderDetailsModel.Status>()
    val error = MutableLiveData<Throwable>()

    fun begin(orderId: String) {
        sendEventUseCase.execute(orderId, Event.Begin, EmptyEventPayload(), {
            orderStatus.postValue(mapOrderDetailsModelStatus(it))
        }, {
            error.postValue(it)
        })
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val sendEventUseCase: SendEventUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return BeginViewModel(sendEventUseCase) as T
        }
    }
}

