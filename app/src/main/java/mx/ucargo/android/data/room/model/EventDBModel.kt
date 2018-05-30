package mx.ucargo.android.data.room.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

const val STATUS_PENDING = 1
const val STATUS_SENT = 2

const val EVENT_QUOTE = "Quote"

@Entity(tableName = "Event")
data class EventDBModel(
        @PrimaryKey(autoGenerate = true) var id: Long = 0,
        var status: Int = STATUS_PENDING,
        var event: String = EVENT_QUOTE,
        var uuid: String = ""
)
