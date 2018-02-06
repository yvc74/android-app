package mx.ucargo.android.Data.Retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface UCargoApiService{

    @POST("user/new")
    fun signUpUser(@Body user: UserDataModel): Call<UserDataModel>
}