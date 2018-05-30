package mx.ucargo.android.data.retrofit

import android.os.Build
import dagger.Module
import dagger.Provides
import mx.ucargo.android.BuildConfig
import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.data.ApiGateway
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class RetrofitModule {
    @Provides
    @Singleton
    fun provideApiGateway(retrofitOrderRepository: RetrofitApiGateway): ApiGateway = retrofitOrderRepository

    @Provides
    @Singleton
    fun provideRemoteOrderRepository(uCargoApiService: UCargoApiService, accountStorage: AccountStorage) = RetrofitApiGateway(uCargoApiService, accountStorage)

    @Provides
    @Singleton
    fun provideUCargoApiService(retrofit: Retrofit) = retrofit.create<UCargoApiService>(UCargoApiService::class.java)

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
    fun provideRetrofit(client: OkHttpClient) = Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://misaelpc.com/api/v1/")
            .build()
}
