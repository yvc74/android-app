package mx.ucargo.android.data.retrofit

import android.os.Build
import dagger.Module
import dagger.Provides
import mx.ucargo.android.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class RetrofitGmapModule {

    @Provides
    @Singleton
    fun provideRoutesRepository(googleMapsApiService: GoogleMapsApiService) = RetrofitGoogleMapsApiGateway(googleMapsApiService)

    @Provides
    @Singleton
    fun provideGmapsApiService(retrofit: Retrofit) = retrofit.create<GoogleMapsApiService>(GoogleMapsApiService::class.java)

    /*@Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val userAgent = "${BuildConfig.APPLICATION_ID}/${BuildConfig.VERSION_NAME} (Linux; Android ${Build.VERSION.SDK_INT})"

        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(GmapsApiKeyInterceptor("AIzaSyBkW9mbE7uBHqty5MhWTnZOrCCbBR5V_Dw"))
                .build()
    }*/

    @Provides
    @Singleton
    fun provideRetrofitGoogleMaps() = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .build()

}


