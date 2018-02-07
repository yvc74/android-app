package mx.ucargo.android.data.sharedpreferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import mx.ucargo.android.data.AccountStorage
import javax.inject.Singleton

@Module
class SharedPreferencesModule {
    @Provides
    @Singleton
    fun provideGson() = Gson()

    @Provides
    @Singleton
    fun provideSharedPreferences(contet: Context) = contet.getSharedPreferences("ucargo", MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideAccountRepository(sharedPreferences: SharedPreferences, gson: Gson): AccountStorage =
            AccountStorageImpl(sharedPreferences, gson)
}
