package mx.ucargo.android.usecase

import dagger.Module
import dagger.Provides
import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.data.ApiGateway
import mx.ucargo.android.data.EventQueue
import mx.ucargo.android.data.GoogleMapsApiGateway

@Module
class UseCaseModule {
    @Provides
    fun provideSignInUseCase(uCargoGateway: ApiGateway, accountStorage: AccountStorage): SignInUseCase =
            SignInUseCaseImpl(uCargoGateway, accountStorage)

    @Provides
    fun provideSignOutUseCase(accountStorage: AccountStorage): SignOutUseCase =
            SignOutUseCaseImpl(accountStorage)

    @Provides
    fun provideGetAccountUseCase(accountStorage: AccountStorage): GetAccountUseCase =
            GetAccountUseCaseImpl(accountStorage)

    @Provides
    fun provideUpdateAccoutStatsUseCase(uCargoGateway: ApiGateway, accountStorage: AccountStorage): UpdateAccoutStatsUseCase =
            UpdateAccoutStatsUseCaseImpl(uCargoGateway, accountStorage)

    @Provides
    fun provideSendEditProfileUseCaseImpl(apiGateway: ApiGateway, accountStorage: AccountStorage): SendEditProfileUseCase = SendEditProfileUseCaseImpl(apiGateway, accountStorage)

    @Provides
    fun provideGetOrderUseCase(apiGateway: ApiGateway): GetOrderUseCase = GetOrderUseCaseImpl(apiGateway)

    @Provides
    fun provideGetOrderListUseCase(apiGateway: ApiGateway): GetOrderListUseCase = GetOrderListUseCaseImpl(apiGateway)

    @Provides
    fun provideSendEventUseCase(apiGateway: ApiGateway, eventQueue: EventQueue): SendEventUseCase = SendEventUseCaseImpl(apiGateway, eventQueue)

    @Provides
    fun provideGetRouteUseCase(googleMapsApiGateway: GoogleMapsApiGateway) : GetRouteUseCase = GetRouteUseCaseImpl(googleMapsApiGateway)

}
