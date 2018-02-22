package mx.ucargo.android.data

import mx.ucargo.android.entity.Event
import mx.ucargo.android.entity.EventPayload

interface EventQueue {
    fun enqueue(orderId: String, event: Event, payload: EventPayload)
}

class EventQueueImpl() : EventQueue {
    override fun enqueue(orderId: String, event: Event, payload: EventPayload) {
        // Do nothing
    }
}
