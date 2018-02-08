package mx.ucargo.android.data.retrofit

import dagger.Module
import dagger.Provides
import mx.ucargo.android.data.OrderRepository
import mx.ucargo.android.data.UCargoGateway
import org.mockito.Mockito.mock
import javax.inject.Singleton

@Module
class TestRetrofitModule {
    @Provides
    @Singleton
    fun provideUCargoGateway(): UCargoGateway {
        return mock(UCargoGateway::class.java)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(): OrderRepository {
        return mock(OrderRepository::class.java)
    }
}
