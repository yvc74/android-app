package mx.ucargo.android.orderdetails

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import mx.ucargo.android.entity.Order
import mx.ucargo.android.entity.Route
import mx.ucargo.android.orderdetails.Mappers.mapOrderDetailsModel
import mx.ucargo.android.usecase.GetOrderUseCase
import mx.ucargo.android.usecase.GetRouteUseCase
import java.util.*

class OrderDetailsViewModel(private val getOrderUseCase: GetOrderUseCase,
                            private val getRouteUseCase: GetRouteUseCase,
                            private val reference: () -> Date) : ViewModel() {
    val order = MutableLiveData<OrderDetailsModel>()
    val error = MutableLiveData<Throwable>()
    val routes = MutableLiveData<List<Route>>()
    val currentLocation = MutableLiveData<Location>()

    fun getOrder(orderId: String) {
        getOrderUseCase.execute(orderId, {
            it.status = Order.Status.Approved
            order.postValue(mapOrderDetailsModel(it, reference.invoke()))
            if (it.type == Order.Type.EXPORT && it.status == Order.Status.New){
                getRouteUseCase.execute("${it.customs.latitude},${it.customs.longitude}","${it.delivery.latitude},${it.delivery.longitude}","${it.pickup.latitude},${it.pickup.longitude}",{
                    routes.postValue(it)
                })
            }
            else if(it.type == Order.Type.IMPORT && it.status == Order.Status.New){
                getRouteUseCase.execute("${it.customs.latitude},${it.customs.longitude}","${it.delivery.latitude},${it.delivery.longitude}",null,{
                    routes.postValue(it)
                })
            }
        }, {
            error.postValue(it)
        })
    }


    fun getRoute(currentLocation:Location,destination :Pair<Double, Double>){
        getRouteUseCase.execute("${currentLocation.latitude},${currentLocation.longitude}","${destination.first},${destination.second}",null,{
            routes.postValue(it)
        })
    }


    fun cancelOrder(orderId: String){

    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val getOrderUseCase: GetOrderUseCase,
                  private val getRouteUseCase: GetRouteUseCase,
                  private val reference: () -> Date) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return OrderDetailsViewModel(getOrderUseCase,getRouteUseCase, reference) as T
        }
    }
}
