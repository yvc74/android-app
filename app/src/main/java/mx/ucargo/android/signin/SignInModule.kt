package mx.ucargo.android.signin

import dagger.Module
import dagger.Provides
import mx.ucargo.android.usecase.GetAccountUseCase
import mx.ucargo.android.usecase.SignInUseCase
import mx.ucargo.android.usecase.SignOutUseCase

@Module
class SignInModule {
    @Provides
    fun provideSignInViewModelFactory(signInUseCase: SignInUseCase, getAccountUseCase: GetAccountUseCase, signOutAccoutUseCase: SignOutUseCase) =
            SignInViewModel.Factory(signInUseCase, getAccountUseCase, signOutAccoutUseCase)
}
