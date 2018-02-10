package mx.ucargo.android.sendquote

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import mx.ucargo.android.entity.Order
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.usecase.SendQuoteUseCase
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

    val sendQuoteUseCase = mock<SendQuoteUseCase>()

    lateinit var sendQuoteViewModel: SendQuoteViewModel

    @Before
    fun setUp() {
        sendQuoteViewModel = SendQuoteViewModel(sendQuoteUseCase)
    }

    @Test
    fun sendQuoteSuccessfully() {
        val captor = argumentCaptor<(Order.Status) -> Unit>()

        sendQuoteViewModel.sendQuote(ANY_INT, ANY_ORDER_ID)

        verify(sendQuoteUseCase).execute(any(), any(), captor.capture(), any())
        captor.firstValue.invoke(Order.Status.SENT_QUOTE)
        assertEquals(OrderDetailsModel.Status.SENT_QUOTE, sendQuoteViewModel.orderStatus.value)
    }

    @Test
    fun sendQuoteUnsuccessfully() {
        val captor = argumentCaptor<(Throwable) -> Unit>()

        sendQuoteViewModel.sendQuote(ANY_INT, ANY_ORDER_ID)

        verify(sendQuoteUseCase).execute(anyInt(), anyString(), any(), captor.capture())
        captor.firstValue.invoke(Throwable())
        assertTrue(sendQuoteViewModel.error.value is Throwable)
    }
}
