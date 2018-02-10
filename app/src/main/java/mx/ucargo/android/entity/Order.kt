package mx.ucargo.android.entity

import java.util.*

data class Order(
        var id: String = "",
        var origin: Location = Location(),
        var destination: Location = Location(),
        var type: Type = Type.IMPORT,
        var quoteDeadline: Date = Date(),
        var status: Status = Status.NEW,
        var details: List<Detail> = emptyList()
) {
    enum class Type { IMPORT, EXPORT }
    data class Detail(var icon: Int = 0,
                      var name: Int = 0,
                      var value: String = "")

    enum class Status {
        NEW,
        SENT_QUOTE
    }
}
