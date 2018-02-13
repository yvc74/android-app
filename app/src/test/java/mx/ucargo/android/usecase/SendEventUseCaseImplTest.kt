package mx.ucargo.android.usecase

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import mx.ucargo.android.data.OrderRepository
import mx.ucargo.android.entity.EmptyEventPayload
import mx.ucargo.android.entity.Event
import mx.ucargo.android.entity.Order
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SendEventUseCaseImplTest {
    private val orderRepository = mock<OrderRepository>()

    lateinit var sendEventUseCase: SendEventUseCase

    @Before
    fun setUp() {
        sendEventUseCase = SendEventUseCaseImpl(orderRepository)
    }

    @Test
    fun existingAccount() {
        val success = mock<(Order.Status) -> Unit>()

        sendEventUseCase.execute("ANY_ORDER_ID", Event.Begin, EmptyEventPayload(), success, mock())

        verify(success).invoke(eq(Order.Status.CUSTOMS))
    }
}
