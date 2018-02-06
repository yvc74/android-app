package mx.ucargo.android.signin

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mx.ucargo.android.usecase.SignInUseCase

@Module
class SignInModule {
    @Provides
    fun provideSignInViewModelFactory(signInUseCase: SignInUseCase): SignInViewModel.Factory {
        return SignInViewModel.Factory(signInUseCase)
    }

    @Provides
    fun provideSignInViewModel(activity: SignInActivity, factory: SignInViewModel.Factory): SignInViewModel {
        return ViewModelProviders.of(activity, factory).get(SignInViewModel::class.java)
    }
}
