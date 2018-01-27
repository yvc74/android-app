package mx.ucargo.android.app

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import dagger.Module
import dagger.Provides
import mx.ucargo.android.usecase.SignUpUseCase
import javax.inject.Singleton

@Module()
class AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

}
