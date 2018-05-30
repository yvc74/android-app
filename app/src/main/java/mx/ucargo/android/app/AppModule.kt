package mx.ucargo.android.app

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import mx.ucargo.android.orderdetails.DateProvider
import mx.ucargo.android.usecase.ListenToQueuedEventsUseCase
import java.util.*
import javax.inject.Singleton

@Module(includes = [Binder::class])
class AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideEventQueue(listenToQueuedEventsUseCase: ListenToQueuedEventsUseCase) =
            EventQueue(listenToQueuedEventsUseCase)

    @Provides
    @Singleton
    fun provideDate() = object : DateProvider {
        override fun invoke(): Date {
            return Date()
        }
    }
}
