package mx.ucargo.android.orderlist

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import mx.ucargo.android.editprofile.Profile
import mx.ucargo.android.entity.Account
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.orderlist.Mappers.mapOrderDetailsModel
import mx.ucargo.android.usecase.GetOrderListUseCase
import mx.ucargo.android.usecase.UpdateAccoutStatsUseCase

class OrderListViewModel(private val getOrderListUseCase: GetOrderListUseCase,private val  updateAccoutStatsUseCase: UpdateAccoutStatsUseCase) : ViewModel() {
    val orderList = MutableLiveData<List<OrderDetailsModel>>()
    val error = MutableLiveData<Throwable>()
    val loading = MutableLiveData<Boolean>()
    val profile = MutableLiveData<Profile>()
    


    init {
        updateAccoutStatsUseCase.execute({
            profile.postValue(it.toProfile())
        }, {

        })
    }

    fun getOrderList(type_order :Int) {
        loading.postValue(true)
        getOrderListUseCase.execute(type_order,{
            orderList.postValue(it.map { mapOrderDetailsModel(it) })
            loading.postValue(false)
        }, {
            error.postValue(it)
            loading.postValue(false)
        })
    }

    class Factory(private val getOrderListUseCase: GetOrderListUseCase,private val updateAccoutStatsUseCase: UpdateAccoutStatsUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return OrderListViewModel(getOrderListUseCase,updateAccoutStatsUseCase) as T
        }
    }
}

private fun Account.toProfile() = Profile(
        name = name,
        username = username,
        email = email,
        picture = picture,
        score = score,
        phone = phone
)

