package mx.ucargo.android.entity

import java.util.*

data class Order(
        var origin: Location = Location(),
        var destination: Location = Location(),
        var type: Type = Type.IMPORT,
        var quoteDeadline: Date = Date()
) {
    enum class Type { IMPORT, EXPORT }
}
