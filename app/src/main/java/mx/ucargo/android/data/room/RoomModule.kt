package mx.ucargo.android.data.room

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import mx.ucargo.android.data.DatabaseGateway
import javax.inject.Singleton

@Module
class RoomModule {
    @Provides
    @Singleton
    fun providesAppDatabase(context: Context): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "database").build()

    @Provides
    @Singleton
    fun providesDatabaseDao(database: AppDatabase) = database.databaseDao()

    @Provides
    @Singleton
    fun provideDatabaseGateway(databaseDao: DatabaseDao): DatabaseGateway = RoomDatabase(databaseDao)
}
