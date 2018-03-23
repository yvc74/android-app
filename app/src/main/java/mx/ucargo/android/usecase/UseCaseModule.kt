package mx.ucargo.android.usecase

import dagger.Module
import dagger.Provides
import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.data.OrderRepository
import mx.ucargo.android.data.UCargoGateway

@Module
class UseCaseModule {
    @Provides
    fun provideSignInUseCase(uCargoGateway: UCargoGateway, accountStorage: AccountStorage): SignInUseCase =
            SignInUseCaseImpl(uCargoGateway, accountStorage)

    @Provides
    fun provideGetAccountUseCase(accountStorage: AccountStorage): GetAccountUseCase =
            GetAccountUseCaseImpl(accountStorage)

    @Provides
    fun provideGetOrderUseCase(orderRepository: OrderRepository): GetOrderUseCase = GetOrderUseCaseImpl(orderRepository)

    @Provides
    fun provideGetOrderListUseCase(orderRepository: OrderRepository): GetOrderListUseCase = GetOrderListUseCaseImpl(orderRepository)

    @Provides
    fun provideSendEventUseCase(orderRepository: OrderRepository): SendEventUseCase = SendEventUseCaseImpl(orderRepository)
}
