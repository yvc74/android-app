package mx.ucargo.android.data.retrofit

import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.data.OrderRepository
import mx.ucargo.android.data.retrofit.Mappers.mapOrder
import mx.ucargo.android.data.retrofit.Mappers.mapOrderDetailDataModel
import mx.ucargo.android.data.retrofit.Mappers.mapQuoteDataModel
import mx.ucargo.android.data.retrofit.model.OrdersResponseDataModel
import mx.ucargo.android.entity.Order
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepositoryImpl(private val uCargoApiService: UCargoApiService,
                          private val accountStorage: AccountStorage) : OrderRepository {
    override fun findById(orderId: String, success: (Order) -> Unit, failure: (Throwable) -> Unit) {
        uCargoApiService.orders(accountStorage.get().token).enqueue(object : Callback<OrdersResponseDataModel?> {
            override fun onResponse(call: Call<OrdersResponseDataModel?>?, response: Response<OrdersResponseDataModel?>?) {
                response?.let {
                    if (it.isSuccessful && it.body() is OrdersResponseDataModel) {
                        success.invoke(mapOrder(it.body()!!))
                    } else {
                        failure.invoke(Throwable())
                    }
                }
            }

            override fun onFailure(call: Call<OrdersResponseDataModel?>?, t: Throwable?) {
                t?.let { failure.invoke(it) }
            }
        })
    }

    override fun persistQuote(quote: Int, orderId: String, success: () -> Unit, failure: (Throwable) -> Unit) {
        uCargoApiService.sendQuote(mapQuoteDataModel(quote), orderId, accountStorage.get().token).enqueue(object : Callback<Any?> {
            override fun onResponse(call: Call<Any?>?, response: Response<Any?>?) {
                response?.let {
                    if (it.isSuccessful) {
                        success.invoke()
                    } else {
                        failure.invoke(Throwable())
                        success.invoke()
                    }
                }
            }

            override fun onFailure(call: Call<Any?>?, t: Throwable?) {
                t?.let { failure.invoke(it) }
            }
        })
    }
    override fun getOrderList(success: (ordersList: List<Order>) -> Unit, failure: (Throwable) -> Unit) {
        uCargoApiService.orders(accountStorage.get().token).enqueue(object : Callback<OrdersResponseDataModel>{
            override fun onResponse(call: Call<OrdersResponseDataModel>?, response: Response<OrdersResponseDataModel>?) {
                response?.let {
                    if (it.isSuccessful && it.body() is OrdersResponseDataModel){
                        //success.invoke(it.body()!!.orders.map { mapOrderDetailDataModel(it) })
                    }
                }
            }

            override fun onFailure(call: Call<OrdersResponseDataModel>?, t: Throwable?) {
                t?.let { failure(it) }
            }
        })
    }
}
