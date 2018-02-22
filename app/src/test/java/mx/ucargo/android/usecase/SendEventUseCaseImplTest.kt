package mx.ucargo.android.usecase

import mx.ucargo.android.data.ApiGateway
import mx.ucargo.android.data.EventQueue
import mx.ucargo.android.entity.EmptyEventPayload
import mx.ucargo.android.entity.Event
import mx.ucargo.android.entity.Order
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SendEventUseCaseImplTest {
    @Mock
    lateinit var orderRepository: ApiGateway

    @Mock
    lateinit var eventQueue: EventQueue

    lateinit var sendEventUseCase: SendEventUseCaseImpl

    @Before
    fun setUp() {
        sendEventUseCase = SendEventUseCaseImpl(orderRepository, eventQueue)
    }

    @Test
    fun existingAccount() {
        val status = sendEventUseCase.executeSync("ANY_ORDER_ID", Event.Begin, EmptyEventPayload())

        assertThat(status, `is`(equalTo(Order.Status.CUSTOMS)))
    }
}
