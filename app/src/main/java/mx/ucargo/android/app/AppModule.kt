package mx.ucargo.android.app

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import dagger.Module
import dagger.Provides
import mx.ucargo.android.signup.SignUpViewModel
import javax.inject.Singleton

@Module()
class AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideSignUpViewModelFactory(): ViewModelProvider.Factory {
        return SignUpViewModel.Factory()
    }


}
