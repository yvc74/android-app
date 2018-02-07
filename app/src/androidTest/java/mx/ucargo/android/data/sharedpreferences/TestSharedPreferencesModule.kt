package mx.ucargo.android.data.sharedpreferences

import dagger.Module
import dagger.Provides
import mx.ucargo.android.data.AccountStorage
import org.mockito.Mockito.mock
import javax.inject.Singleton

@Module
class TestSharedPreferencesModule {
    @Provides
    @Singleton
    fun provideAccountRepository(): AccountStorage {
        return mock(AccountStorage::class.java)
    }
}
