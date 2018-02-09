package mx.ucargo.android.orderdetails

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import mx.ucargo.android.entity.Order
import mx.ucargo.android.orderdetails.Mappers.mapOrderDetailsModel
import mx.ucargo.android.usecase.GetOrderUseCase

class OrderDetailsViewModel(private val getOrderUseCase: GetOrderUseCase) : ViewModel() {
    val order = MutableLiveData<OrderDetailsModel>()
    val error = MutableLiveData<Throwable>()

    init {
        order.value = OrderDetailsModel(
                originName = "Mexico",
                originLatLng = Pair(19.432608, -99.133209),
                destinationName = "Veracruz",
                destinationLatLng = Pair(19.173773, -96.134224),
                orderType = Order.Type.IMPORT,
                remainingTime = "3 days",
                pickUpAddress = "Line 1\nLine 2\nLine 3",
                deliverAddress = "Line 1\nLine 2\nLine 3",
                details = listOf(
                        OrderDetailModel(name = "Distance", value = "153,973 Km"),
                        OrderDetailModel(name = "Transport", value = "Platform"),
                        OrderDetailModel(name = "Order Number", value = "58362RD4"),
                        OrderDetailModel(name = "Content", value = "Electronics"),
                        OrderDetailModel(name = "Weight", value = "156.49 Kg"),
                        OrderDetailModel(name = "Ref Master", value = "12F56"),
                        OrderDetailModel(name = "Ref House", value = "23H7"),
                        OrderDetailModel(name = "Ref uCargo", value = "000876"),
                        OrderDetailModel(name = "Sender", value = "Steren S.A. de C.V"))
        )
    }

    fun getOrder(orderId: String) {
        getOrderUseCase.execute(orderId, {
            order.postValue(mapOrderDetailsModel(it))
        }, {
            error.postValue(it)
        })
    }

    class Factory(private val getOrderUseCase: GetOrderUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return OrderDetailsViewModel(getOrderUseCase) as T
        }

    }
}
