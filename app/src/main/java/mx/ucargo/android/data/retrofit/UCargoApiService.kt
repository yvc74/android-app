package mx.ucargo.android.data.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface UCargoApiService {
    @GET("drivers/account")
    fun signIn(@Header("Authorization") authorization: String): Call<AccountDataModel>
}
