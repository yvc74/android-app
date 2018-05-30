package mx.ucargo.android.app

import android.util.Log
import mx.ucargo.android.usecase.ListenToQueuedEventsUseCase

private val TAG = EventQueue::class.java.simpleName

class EventQueue(listenToQueuedEventsUseCase: ListenToQueuedEventsUseCase) {
    init {
        listenToQueuedEventsUseCase.execute {
            Log.d(TAG, "$it")
        }
    }
}
