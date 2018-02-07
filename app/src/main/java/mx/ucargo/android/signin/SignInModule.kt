package mx.ucargo.android.signin

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mx.ucargo.android.usecase.GetAccountUseCase
import mx.ucargo.android.usecase.SignInUseCase

@Module
class SignInModule {
    @Provides
    fun provideSignInViewModelFactory(signInUseCase: SignInUseCase, getAccountUseCase: GetAccountUseCase) =
            SignInViewModel.Factory(signInUseCase, getAccountUseCase)

    @Provides
    fun provideSignInViewModel(activity: SignInActivity, factory: SignInViewModel.Factory): SignInViewModel =
            ViewModelProviders.of(activity, factory).get(SignInViewModel::class.java)

}
