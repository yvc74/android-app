package mx.ucargo.android.entity

import java.util.*

data class Order(
        var id: String = "",
        var pickup: Location = Location(),
        var delivery: Location = Location(),
        var type: Type = Type.IMPORT,
        var quoteDeadline: Date = Date(),
        var status: Status = Status.NEW,
        var details: List<Detail> = emptyList(),
        var quote: Int = 0
) {
    enum class Type { IMPORT, EXPORT }
    data class Detail(var icon: String = "",
                      var label: String = "",
                      var value: String = "")

    enum class Status {
        NEW,
        SENT_QUOTE,
        APPROVED,
        CUSTOMS
    }
}
