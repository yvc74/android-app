package mx.ucargo.android.entity

import java.util.*

data class Order(
        var origin: Location = Location(),
        var destination: Location = Location(),
        var type: Type = Type.IMPORT,
        var quoteDeadline: Date = Date(),
        var details: List<Detail> = emptyList()
) {
    enum class Type { IMPORT, EXPORT }
    data class Detail(var icon: String = "",
                      var name: Int = 0,
                      var value: String = "")
}
