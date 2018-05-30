package mx.ucargo.android.data.room.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

const val ORDER_TYPE_IMPORT = 1
const val ORDER_TYPE_EXPORT = 2

const val ORDER_STATUS_NEW = 1
const val ORDER_STATUS_SENT_QUOTE = 2
const val ORDER_STATUS_APPROVED = 3
const val ORDER_STATUS_CUSTOMS = 4

@Entity(tableName = "Order")
data class OrderDBModel(
        @PrimaryKey
        var id: String = "",

        @Embedded(prefix = "pickup")
        var pickup: LocationDBModel = LocationDBModel(),

        @Embedded(prefix = "delivery")
        var delivery: LocationDBModel = LocationDBModel(),

        var type: Int = ORDER_TYPE_IMPORT,
        var quoteDeadline: String = "",
        var status: Int = ORDER_STATUS_NEW,
        var quote: Int = 0
)
