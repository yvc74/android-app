package mx.ucargo.android.Data.Retrofit

import dagger.Module
import dagger.Provides
import mx.ucargo.android.Data.SignUpGateway
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by noeperezchamorro on 26/01/18.
 */
@Module
class RetrofitModule {
    @Provides
    @Singleton
    fun provideSignUpGateway(signUpService: SignUpService): SignUpGateway = RetrofitSignUp(signUpService)

    @Provides
    @Singleton
    fun provideSignUpService(retrofit: Retrofit) = retrofit.create<SignUpService>(SignUpService::class.java)


    @Provides
    @Singleton
    fun provideRetrofit() = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("").build()
}