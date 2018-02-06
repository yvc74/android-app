package mx.ucargo.android.usecase

import dagger.Module
import dagger.Provides
import mx.ucargo.android.data.UCargoGateway


@Module
class UseCaseModule {
    @Provides
    fun provideSignInUseCase(uCargoGateway: UCargoGateway): SignInUseCase = SignInUseCaseImpl(uCargoGateway)
}
