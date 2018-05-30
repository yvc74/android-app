package mx.ucargo.android.customscheck

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import mx.ucargo.android.orderdetails.OrderDetailsModel
import javax.inject.Inject

class CustomsCheckViewModel @Inject constructor() : ViewModel() {
    val orderStatus = MutableLiveData<OrderDetailsModel.Status>()
    val error = MutableLiveData<Throwable>()

    fun greenLight(orderId: String) {

    }
}
