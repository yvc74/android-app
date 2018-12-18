package mx.ucargo.android.reportlocationtocustom

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.location.Location
import mx.ucargo.android.entity.EmptyEventPayload
import mx.ucargo.android.entity.Event
import mx.ucargo.android.entity.EventPayload
import mx.ucargo.android.entity.ReportLocationEventPayload
import mx.ucargo.android.orderdetails.Mappers
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.usecase.SendEventUseCase


class ReportLocationViewModel(private val sendEventUseCase: SendEventUseCase) : ViewModel() {
    val orderStatus = MutableLiveData<OrderDetailsModel.Status>()
    val error = MutableLiveData<Throwable>()

    fun sendLocationEvent(orderId: String,mcurrentLocation: Location) {
        sendEventUseCase.execute(orderId, Event.ReportLocation, ReportLocationEventPayload(mcurrentLocation), {
            orderStatus.postValue(Mappers.mapOrderDetailsModelStatus(it))
        }, {
            error.postValue(it)
        })
    }

    fun startSignEvent(orderId: String) {
        sendEventUseCase.execute(orderId, Event.ReportSign, EmptyEventPayload(),{
            orderStatus.postValue(Mappers.mapOrderDetailsModelStatus(it))
        },{

        })
    }


    @Suppress("UNCHECKED_CAST")
    class Factory(private val sendEventUseCase: SendEventUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ReportLocationViewModel(sendEventUseCase) as T
        }
    }
}
