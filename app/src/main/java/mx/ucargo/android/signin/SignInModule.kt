package mx.ucargo.android.signin

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides

@Module
class SignInModule {
    @Provides
    fun provideSignInViewModelFactory(): SignInViewModel.Factory {
        return SignInViewModel.Factory()
    }

    @Provides
    fun provideSignInViewModel(activity: SignInActivity, factory: SignInViewModel.Factory): SignInViewModel {
        return ViewModelProviders.of(activity, factory).get(SignInViewModel::class.java)
    }
}
