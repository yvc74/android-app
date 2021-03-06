package mx.ucargo.android.signup

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides

@Module
class SignUpModule {
    @Provides
    fun provideSignUpViewModelFactory(): SignUpViewModel.Factory {
        return SignUpViewModel.Factory()
    }

    @Provides
    fun provideSignUpViewModel(activity: SignUpActivity, factory: SignUpViewModel.Factory): SignUpViewModel {
        return ViewModelProviders.of(activity, factory).get(SignUpViewModel::class.java)
    }
}
