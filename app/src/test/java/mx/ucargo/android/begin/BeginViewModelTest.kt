package mx.ucargo.android.begin

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import mx.ucargo.android.entity.Event
import mx.ucargo.android.entity.Order
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.usecase.SendEventUseCase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val ANY_ORDER_ID = "ANY_ORDER_ID"

@RunWith(MockitoJUnitRunner::class)
class BeginViewModelTest {

    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()

    @Mock
    lateinit var sendEventUseCase: SendEventUseCase

    lateinit var beginViewModel: BeginViewModel

    @Before
    fun setUp() {
        beginViewModel = BeginViewModel(sendEventUseCase)
    }

    @Test
    fun sendQuoteSuccessfully() {
        val captor = argumentCaptor<(Order.Status) -> Unit>()

        beginViewModel.begin(ANY_ORDER_ID)

        verify(sendEventUseCase).execute(any(), eq(Event.Begin), any(), captor.capture(), any())
        captor.firstValue.invoke(Order.Status.CUSTOMS)
        assertEquals(OrderDetailsModel.Status.CUSTOMS, beginViewModel.orderStatus.value)
    }
}
