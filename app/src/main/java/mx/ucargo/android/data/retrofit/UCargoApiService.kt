package mx.ucargo.android.data.retrofit

import mx.ucargo.android.data.retrofit.model.OrdersResponseDataModel
import mx.ucargo.android.data.retrofit.model.QuoteDataModel
import mx.ucargo.android.data.retrofit.model.SignInResponseDataModel
import retrofit2.Call
import retrofit2.http.*

interface UCargoApiService {
    @GET("drivers/account")
    fun signIn(@Header("Authorization") authorization: String): Call<SignInResponseDataModel>

    @GET("drivers/orders")
    fun orders(@Header("x-auth-token") token: String): Call<OrdersResponseDataModel>

    @POST("drivers/orders/{orderId}/quotes\n")
    fun sendQuote(@Body quoteDataModel: QuoteDataModel, @Path("orderId") orderId: String, @Header("x-auth-token") token: String): Call<Any>
}
