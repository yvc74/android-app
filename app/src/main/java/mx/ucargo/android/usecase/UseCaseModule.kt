package mx.ucargo.android.usecase

import dagger.Module
import dagger.Provides
import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.data.ApiGateway
import mx.ucargo.android.data.DatabaseGateway

@Module
class UseCaseModule {
    @Provides
    fun provideSignInUseCase(uCargoGateway: ApiGateway, accountStorage: AccountStorage): SignInUseCase =
            SignInUseCaseImpl(uCargoGateway, accountStorage)

    @Provides
    fun provideGetAccountUseCase(accountStorage: AccountStorage): GetAccountUseCase =
            GetAccountUseCaseImpl(accountStorage)

    @Provides
    fun provideGetOrderUseCase(apiGateway: ApiGateway, databaseGateway: DatabaseGateway): GetOrderUseCase =
            GetOrderUseCaseImpl(apiGateway, databaseGateway)

    @Provides
    fun provideGetOrderListUseCase(apiGateway: ApiGateway, databaseGateway: DatabaseGateway): ListenToOrderListUseCase =
            ListenToOrderListUseCaseImpl(apiGateway, databaseGateway)

    @Provides
    fun provideSendEventUseCase(apiGateway: ApiGateway, databaseGateway: DatabaseGateway): SendEventUseCase =
            SendEventUseCaseImpl(apiGateway, databaseGateway)

    @Provides
    fun provideListenToQueuedEventsUseCase(databaseGateway: DatabaseGateway): ListenToQueuedEventsUseCase =
            ListenToQueuedEventsUseCaseImpl(databaseGateway)
}
