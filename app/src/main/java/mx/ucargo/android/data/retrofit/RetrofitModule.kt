package mx.ucargo.android.data.retrofit

import android.os.Build
import dagger.Module
import dagger.Provides
import mx.ucargo.android.BuildConfig
import mx.ucargo.android.data.AccountStorage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
class RetrofitModule {
    @Provides
    @Singleton
    fun provideRemoteOrderRepository(uCargoApiService: UCargoApiService, accountStorage: AccountStorage) = RetrofitApiGateway(uCargoApiService, accountStorage)

    @Provides
    @Singleton
    fun provideUCargoApiService(@Named("ucargo") retrofit: Retrofit) = retrofit.create<UCargoApiService>(UCargoApiService::class.java)

    @Provides
    @Singleton
    fun provideRoutesRepository(googleMapsApiService: GoogleMapsApiService) = RetrofitGoogleMapsApiGateway(googleMapsApiService)

    @Provides
    @Singleton
    fun provideGmapsApiService(@Named("gmaps")retrofit: Retrofit) = retrofit.create<GoogleMapsApiService>(GoogleMapsApiService::class.java)


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val userAgent = "${BuildConfig.APPLICATION_ID}/${BuildConfig.VERSION_NAME} (Linux; Android ${Build.VERSION.SDK_INT})"

        return OkHttpClient.Builder()
                .addInterceptor(UserAgentInterceptor(userAgent))
                .addInterceptor(loggingInterceptor)
                .addInterceptor(ApiKeyInterceptor("e70e918f-8035-48fc-a707-4507e1fd85c1"))
                .build()
    }



    @Provides
    @Singleton
    @Named("ucargo")
    fun provideRetrofit(client: OkHttpClient) = Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://misaelpc.com/api/v1/")
            .build()

    @Provides
    @Singleton
    @Named("gmaps")
    fun provideRetrofitGoogleMaps() = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .build()
}
