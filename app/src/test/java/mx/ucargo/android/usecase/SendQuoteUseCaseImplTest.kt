package mx.ucargo.android.usecase

import com.nhaarman.mockito_kotlin.*
import mx.ucargo.android.data.OrderRepository
import mx.ucargo.android.entity.EmptyQuote
import mx.ucargo.android.entity.Order
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString

@RunWith(JUnit4::class)
class SendQuoteUseCaseImplTest {
    val ANY_INT = 1
    val ANY_ORDER_ID = "ANY_ORDER_ID"

    val orderRepository = mock<OrderRepository>()

    lateinit var sendQuoteUseCase: SendQuoteUseCase

    @Before
    fun setUp() {
        sendQuoteUseCase = SendQuoteUseCaseImpl(orderRepository)
    }

    @Test
    fun sendQuoteSuccessfully() {
        var success = mock<(Order.Status) -> Unit>()
        val captor = argumentCaptor<() -> Unit>()

        sendQuoteUseCase.execute(ANY_INT, ANY_ORDER_ID, success, mock())

        verify(orderRepository).persistQuote(anyInt(), anyString(), captor.capture(), any())
        captor.firstValue.invoke()
        verify(success).invoke(eq(Order.Status.SENT_QUOTE))
    }

    @Test
    fun sendZeroQuote() {
        val ZERO = 0
        var failure = mock<(Throwable) -> Unit>()

        sendQuoteUseCase.execute(ZERO, ANY_ORDER_ID, mock(), failure)

        verify(failure).invoke(any<EmptyQuote>())
    }

    @Test
    fun sendQuoteUnsuccessfully() {
        var failure = mock<(Throwable) -> Unit>()
        val captor = argumentCaptor<(Throwable) -> Unit>()

        sendQuoteUseCase.execute(ANY_INT, ANY_ORDER_ID, mock(), failure)

        verify(orderRepository).persistQuote(anyInt(), anyString(), any(), captor.capture())
        captor.firstValue.invoke(Throwable())
        verify(failure).invoke(any())
    }
}
