package mx.ucargo.android.usecase

import dagger.Module
import dagger.Provides
import mx.ucargo.android.Data.UCargoGateway


@Module
class UseCaseModule {

    @Provides
    fun provideSignUpUseCase(uCargoGateway: UCargoGateway): SignUpUseCase = SignUpUseCaseImpl(uCargoGateway)

}
