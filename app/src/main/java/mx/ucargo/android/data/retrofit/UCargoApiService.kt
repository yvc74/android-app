package mx.ucargo.android.data.retrofit

import android.accounts.Account
import mx.ucargo.android.data.retrofit.model.*
import retrofit2.Call
import retrofit2.http.*

interface UCargoApiService {
    @GET("drivers/account")
    fun signIn(@Header("Authorization") authorization: String): Call<SignInResponseDataModel>

    @PATCH("drivers/account")
    fun editDriverAccount(@Header("x-auth-token") token: String, @Body hashMap: HashMap<String,AccountDataModel>): Call<EditProfileResponseDataModel>

    @GET("drivers/orders")
    fun orders(@Header("x-auth-token") token: String): Call<OrdersResponseDataModel>

    @GET("drivers/orders")
    fun ordersLog(@Header("x-auth-token") token: String,@Query("status") order : String): Call<OrdersResponseDataModel>

    @POST("drivers/orders/{orderId}/quote")
    fun sendQuote(@Body quoteDataModel: QuoteDataModel, @Path("orderId") orderId: String, @Header("x-auth-token") token: String): Call<Any>

    @POST("drivers/orders/{orderId}/event")
    fun sendEventQuote(@Body quoteDataModel: QuoteDataModel, @Path("orderId") orderId: String, @Header("x-auth-token") token: String): Call<Any>
}
