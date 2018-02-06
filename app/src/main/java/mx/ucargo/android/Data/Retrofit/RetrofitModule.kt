package mx.ucargo.android.Data.Retrofit

import dagger.Module
import dagger.Provides
import mx.ucargo.android.Data.UCargoGateway
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {
    @Provides
    @Singleton
    fun provideSignUpGateway(uCargoApiService: UCargoApiService): UCargoGateway = RetrofitSignUp(uCargoApiService)

    @Provides
    @Singleton
    fun provideSignUpService(retrofit: Retrofit) = retrofit.create<UCargoApiService>(UCargoApiService::class.java)


    @Provides
    @Singleton
    fun provideRetrofit() = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("").build()
}