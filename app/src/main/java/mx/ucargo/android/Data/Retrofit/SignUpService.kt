package mx.ucargo.android.Data.Retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by noeperezchamorro on 26/01/18.
 */

interface SignUpService{
    @POST("user/new")
    fun signUser(@Body user: UserDataModel): Call<UserDataModel>
}