package mx.ucargo.android.customscheck

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import mx.ucargo.android.entity.EmptyEventPayload
import mx.ucargo.android.entity.Event
import mx.ucargo.android.orderdetails.Mappers
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.usecase.SendEventUseCase

class CustomsCheckViewModel(private val sendEventUseCase: SendEventUseCase) : ViewModel() {
    val orderStatus = MutableLiveData<OrderDetailsModel.Status>()
    val error = MutableLiveData<Throwable>()

    fun greenLight(orderId: String) {
        sendEventUseCase.execute(orderId, Event.Green, EmptyEventPayload(), {
            orderStatus.postValue(Mappers.mapOrderDetailsModelStatus(it))
        }, {
            error.postValue(it)
        })
    }

    fun redLight(orderId: String) {
        sendEventUseCase.execute(orderId, Event.Red, EmptyEventPayload(), {
            orderStatus.postValue(Mappers.mapOrderDetailsModelStatus(it))
        }, {
            error.postValue(it)
        })
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val sendEventUseCase: SendEventUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CustomsCheckViewModel(sendEventUseCase) as T
        }

    }
}
