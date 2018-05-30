package mx.ucargo.android.sendquote

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import mx.ucargo.android.entity.Event
import mx.ucargo.android.entity.QuoteEventPayload
import mx.ucargo.android.orderdetails.Mappers.mapOrderDetailsModelStatus
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.usecase.SendEventUseCase
import javax.inject.Inject

class SendQuoteViewModel @Inject constructor(private val sendEventUseCase: SendEventUseCase) : ViewModel() {
    val orderStatus = MutableLiveData<OrderDetailsModel.Status>()
    val error = MutableLiveData<Throwable>()

    fun sendQuote(quote: Int, orderId: String) {
        sendEventUseCase.execute(orderId, Event.Quote, QuoteEventPayload(quote), {
            orderStatus.postValue(mapOrderDetailsModelStatus(it))
        }, {
            error.postValue(it)
        })
    }
}
