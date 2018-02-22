package mx.ucargo.android.app

import dagger.Module
import dagger.Provides
import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.data.ApiGateway
import mx.ucargo.android.data.EventQueue
import org.mockito.Mockito
import javax.inject.Singleton

@Module
class MockModule {
    @Provides
    @Singleton
    fun provideApiGateway(): ApiGateway {
        return Mockito.mock(ApiGateway::class.java)
    }

    @Provides
    @Singleton
    fun provideAccountRepository(): AccountStorage {
        return Mockito.mock(AccountStorage::class.java)
    }

    @Provides
    @Singleton
    fun provideEventQueue(): EventQueue {
        return Mockito.mock(EventQueue::class.java)
    }
}
