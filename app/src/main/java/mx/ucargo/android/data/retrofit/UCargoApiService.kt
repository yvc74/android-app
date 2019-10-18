package mx.ucargo.android.data.retrofit

import mx.ucargo.android.data.retrofit.model.*
import retrofit2.Call
import retrofit2.http.*

interface UCargoApiService {
    @GET("drivers/account")
    fun signIn(@Header("Authorization") authorization: String): Call<SignInResponseDataModel>

    @GET("drivers/account/profile")
    fun updateAccount(@Header("x-auth-token") token: String): Call<SignInResponseDataModel>

    @PATCH("drivers/account")
    fun editDriverAccount(@Header("x-auth-token") token: String, @Body hashMap: HashMap<String,AccountDataModel>): Call<EditProfileResponseDataModel>

    @GET("drivers/orders")
    fun orders(@Header("x-auth-token") token: String): Call<OrdersResponseDataModel>

    @GET("drivers/orders/{orderId}")
    fun orderById(@Path("orderId") orderId: String, @Header("x-auth-token") token: String): Call<OrderDataModel>

    @GET("drivers/orders")
    fun ordersNew(@Header("x-auth-token") token: String,@Query("status") order : String): Call<OrdersResponseDataModel>

    @GET("drivers/orders")
    fun ordersLog(@Header("x-auth-token") token: String,@Query("status") order : String): Call<OrdersResponseDataModel>

    @POST("drivers/orders/{orderId}/events")
    fun sendQuote(@Body hashMap: HashMap<String, QuoteEventDataModel>, @Path("orderId") orderId: String, @Header("x-auth-token") token: String): Call<Any>

    @POST("drivers/orders/{orderId}/events")
    fun sendBegin(@Body hashMap: HashMap<String, BeginEventDataModel>, @Path("orderId") orderId: String, @Header("x-auth-token") token: String): Call<Any>

    @POST("drivers/orders/{orderId}/events")
    fun sendCustomType(@Body hashMap: HashMap<String, CustomEventDataModel>, @Path("orderId") orderId: String, @Header("x-auth-token") token: String): Call<Any>


    @POST("drivers/orders/{orderId}/event")
    fun sendEventQuote(@Body eventDataModel: QuoteEventDataModel, @Path("orderId") orderId: String, @Header("x-auth-token") token: String): Call<Any>

    @POST("drivers/orders/{orderId}/events")
    fun sendLockImage(@Body hashMap: HashMap<String, LockEventDataModel>, @Path("orderId") orderId: String, @Header("x-auth-token") token: String): Call<Any>

    @POST("drivers/orders/{orderId}/events")
    fun sendSignImage(@Body hashMap: HashMap<String, SignEventDataModel>, @Path("orderId") orderId: String, @Header("x-auth-token") token: String): Call<Any>

    @POST("drivers/orders/{orderId}/events")
    fun sendLocation(@Body hashMap: HashMap<String, LocationEventDataModel>, @Path("orderId") orderId: String, @Header("x-auth-token") token: String): Call<Any>

    @POST("drivers/orders/{orderId}/events")
    fun sendBeginRoute(@Body hashMap: HashMap<String, EventDataModel>, @Path("orderId") orderId: String, @Header("x-auth-token") token: String): Call<Any>

    @POST("drivers/orders/{orderId}/events")
    fun sendStore(@Body hashMap: HashMap<String, EventDataModel>, @Path("orderId") orderId: String, @Header("x-auth-token") token: String): Call<Any>

    @POST("drivers/orders/{orderId}/events")
    fun sendCollect(@Body hashMap: HashMap<String, EventDataModel>, @Path("orderId") orderId: String, @Header("x-auth-token") token: String): Call<Any>


}
