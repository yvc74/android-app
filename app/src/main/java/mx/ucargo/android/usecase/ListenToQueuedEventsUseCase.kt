package mx.ucargo.android.usecase

import mx.ucargo.android.data.DatabaseGateway
import mx.ucargo.android.entity.QueuedEvent

interface ListenToQueuedEventsUseCase {
    fun execute(listener: ((QueuedEvent) -> Unit)? = null)
}

class ListenToQueuedEventsUseCaseImpl(private val databaseGateway: DatabaseGateway) : ListenToQueuedEventsUseCase {
    override fun execute(listener: ((QueuedEvent) -> Unit)?) {
        databaseGateway.subscribeToNewEvents {
            listener?.invoke(it)
        }
    }
}
