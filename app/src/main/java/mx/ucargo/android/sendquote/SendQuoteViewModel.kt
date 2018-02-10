package mx.ucargo.android.sendquote

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.sendquote.Mappers.mapOrderDetailsModelStatus
import mx.ucargo.android.usecase.SendQuoteUseCase

class SendQuoteViewModel(private val sendQuoteUseCase: SendQuoteUseCase) : ViewModel() {
    val orderStatus = MutableLiveData<OrderDetailsModel.Status>()
    val error = MutableLiveData<Throwable>()

    fun sendQuote(quote: Int, orderId: String) {
        sendQuoteUseCase.execute(quote, orderId, {
            orderStatus.postValue(mapOrderDetailsModelStatus(it))
        }, {
            error.postValue(it)
        })
    }

    class Factory(private val sendQuoteUseCase: SendQuoteUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SendQuoteViewModel(sendQuoteUseCase) as T
        }
    }
}
