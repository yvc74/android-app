package mx.ucargo.android.usecase

import dagger.Module
import dagger.Provides
import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.data.EventQueue
import mx.ucargo.android.data.ApiGateway

@Module
class UseCaseModule {
    @Provides
    fun provideSignInUseCase(uCargoGateway: ApiGateway, accountStorage: AccountStorage): SignInUseCase =
            SignInUseCaseImpl(uCargoGateway, accountStorage)

    @Provides
    fun provideGetAccountUseCase(accountStorage: AccountStorage): GetAccountUseCase =
            GetAccountUseCaseImpl(accountStorage)

    @Provides
    fun provideGetOrderUseCase(apiGateway: ApiGateway): GetOrderUseCase = GetOrderUseCaseImpl(apiGateway)

    @Provides
    fun provideGetOrderListUseCase(apiGateway: ApiGateway): GetOrderListUseCase = GetOrderListUseCaseImpl(apiGateway)

    @Provides
    fun provideSendEventUseCase(apiGateway: ApiGateway, eventQueue: EventQueue): SendEventUseCase = SendEventUseCaseImpl(apiGateway, eventQueue)
}
