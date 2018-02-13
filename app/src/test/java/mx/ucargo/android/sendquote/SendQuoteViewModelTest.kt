package mx.ucargo.android.sendquote

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.*
import mx.ucargo.android.entity.Event
import mx.ucargo.android.entity.Order
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.usecase.SendEventUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString

@RunWith(JUnit4::class)
class SendQuoteViewModelTest {
    val ANY_INT = 1
    val ANY_ORDER_ID = "ANY_ORDER_ID"

    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()

    val sendEventUseCase = mock<SendEventUseCase>()

    lateinit var sendQuoteViewModel: SendQuoteViewModel

    @Before
    fun setUp() {
        sendQuoteViewModel = SendQuoteViewModel(sendEventUseCase)
    }

    @Test
    fun sendQuoteSuccessfully() {
        val captor = argumentCaptor<(Order.Status) -> Unit>()

        sendQuoteViewModel.sendQuote(ANY_INT, ANY_ORDER_ID)

        verify(sendEventUseCase).execute(anyString(), eq(Event.Quote), any(), captor.capture(), any())
        captor.firstValue.invoke(Order.Status.SENT_QUOTE)
        assertEquals(OrderDetailsModel.Status.SENT_QUOTE, sendQuoteViewModel.orderStatus.value)
    }

    @Test
    fun sendQuoteUnsuccessfully() {
        val captor = argumentCaptor<(Throwable) -> Unit>()

        sendQuoteViewModel.sendQuote(ANY_INT, ANY_ORDER_ID)

        verify(sendEventUseCase).execute(anyString(), eq(Event.Quote), any(), any(), captor.capture())
        captor.firstValue.invoke(Throwable())
        assertTrue(sendQuoteViewModel.error.value is Throwable)
    }
}
