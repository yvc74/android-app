package mx.ucargo.android.data

import dagger.Module
import dagger.Provides
import mx.ucargo.android.data.retrofit.RetrofitApiGateway
import mx.ucargo.android.data.retrofit.RetrofitModule
import mx.ucargo.android.data.sharedpreferences.SharedPreferencesModule

@Module(includes = [RetrofitModule::class, SharedPreferencesModule::class])
class DataModule {
    @Provides
    fun providesEventQueue(): EventQueue = EventQueueImpl()

    @Provides
    fun providesOrderRepository(retrofitOrderRepository: RetrofitApiGateway): ApiGateway = retrofitOrderRepository
}
