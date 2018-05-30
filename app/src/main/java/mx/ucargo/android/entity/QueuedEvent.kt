package mx.ucargo.android.entity

data class QueuedEvent(
        var uuid: String = "",
        var event: Event = Event.Quote,
        var status: Status = Status.PENDING
) {
    enum class Status { PENDING, SENT }
}
