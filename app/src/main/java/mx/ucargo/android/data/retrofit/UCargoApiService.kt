package mx.ucargo.android.data.retrofit

import mx.ucargo.android.data.retrofit.model.OrdersResponseDataModel
import mx.ucargo.android.data.retrofit.model.SignInResponseDataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface UCargoApiService {
    @GET("drivers/account")
    fun signIn(@Header("Authorization") authorization: String): Call<SignInResponseDataModel>

    @GET("drivers/orders")
    fun orders(@Header("x-auth-token") token: String): Call<OrdersResponseDataModel>
}
