package mx.ucargo.android.orderdetails

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import mx.ucargo.android.entity.Order
import mx.ucargo.android.orderdetails.Mappers.mapOrderDetailsModel
import mx.ucargo.android.usecase.GetOrderUseCase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString
import java.util.*

@RunWith(JUnit4::class)
class OrderDetailsViewModelTest {
    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()

    val getOrderUseCase = mock<GetOrderUseCase>()

    val reference = {
        val calendar = Calendar.getInstance()
        calendar.set(2018, 1, 9)
        calendar.time
    }

    lateinit var orderDetailsViewModel: OrderDetailsViewModel

    @Before
    fun setUp() {
        orderDetailsViewModel = OrderDetailsViewModel(getOrderUseCase, reference)
    }

    @Test
    fun getOrder() {
        val expectedOrder = Order()
        val captor = argumentCaptor<(Order) -> Unit>()

        orderDetailsViewModel.getOrder("ANY_ORDER_ID")

        verify(getOrderUseCase).execute(anyString(), captor.capture(), any())
        captor.firstValue.invoke(expectedOrder)
        assertEquals(mapOrderDetailsModel(expectedOrder, reference.invoke()), orderDetailsViewModel.order.value)
    }
}
