package mx.ucargo.android.data.room.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "OrderDetail")
class OrderDetailDBModel(
        @PrimaryKey(autoGenerate = true) var id: Long = 0,
        var orderId: String = "",
        var icon: String = "",
        var label: String = "",
        var value: String = ""
)
