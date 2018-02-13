package mx.ucargo.android.usecase

import mx.ucargo.android.entity.Event
import mx.ucargo.android.entity.EventPayload
import mx.ucargo.android.entity.Order

interface SendEventUseCase {
    fun execute(orderId: String, event: Event, eventPayload: EventPayload, success: (Order.Status) -> Unit, failure: (Throwable) -> Unit)
}
