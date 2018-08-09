package mx.ucargo.android.data.retrofit

import mx.ucargo.android.data.retrofit.model.*
import retrofit2.Call
import retrofit2.http.*

interface GoogleMapsApiService {
    @GET("directions/json")
    fun getRoutesImport(@Query("origin") origin : String,@Query("destination") destination : String,@Query("key")apikey:String = "AIzaSyBkW9mbE7uBHqty5MhWTnZOrCCbBR5V_Dw" ): Call<RoutesDataModel>


    @GET("directions/json")
    fun getRoutesExport(@Query("origin") origin : String, @Query("destination") destination : String, @Query("waypoints") pickUp:String, @Query("key")apikey:String = "AIzaSyBkW9mbE7uBHqty5MhWTnZOrCCbBR5V_Dw" ): Call<RoutesDataModel>

}
