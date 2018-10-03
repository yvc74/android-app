package mx.ucargo.android.entity

import java.util.*

data class Order(
        var id: String = "",
        var pickup: Location = Location(),
        var delivery: Location = Location(),
        var customs: Location = Location(),
        var type: Type = Type.IMPORT,
        var quoteDeadline: Date = Date(),
        var status: Status = Status.New,
        var details: List<Detail> = emptyList(),
        var quote: Int = 0
) {
    enum class Type { EXPORT,IMPORT }
    data class Detail(var icon: String = "",
                      var label: String = "",
                      var value: String = "")

    enum class Status {
        New,
        Quoted,
        Approved,
        OnRouteToCustom,
        ReportedGreen,
        ReportedLock,
        Customs,
        Red,
        OnRoute,
        Stored,
        BeginRoute,
        OnTracking,
        ReportedSign,
        Finished
    }
}
