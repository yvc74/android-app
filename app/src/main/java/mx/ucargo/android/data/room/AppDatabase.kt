package mx.ucargo.android.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import mx.ucargo.android.data.room.model.EventDBModel
import mx.ucargo.android.data.room.model.OrderDBModel
import mx.ucargo.android.data.room.model.OrderDetailDBModel

@Database(entities = [
    EventDBModel::class,
    OrderDBModel::class,
    OrderDetailDBModel::class
], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseDao(): DatabaseDao
}
