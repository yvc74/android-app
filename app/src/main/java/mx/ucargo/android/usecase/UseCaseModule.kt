package mx.ucargo.android.usecase

import dagger.Module
import dagger.Provides
import mx.ucargo.android.Data.SignUpGateway


@Module
class UseCaseModule {

    @Provides
    fun provideSignUpUseCase(signUpGateway: SignUpGateway): SignUpUseCase = SignUpUseCaseImpl(signUpGateway)

}
