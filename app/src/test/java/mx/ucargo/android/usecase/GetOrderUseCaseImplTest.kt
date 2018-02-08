package mx.ucargo.android.usecase

import com.nhaarman.mockito_kotlin.*
import mx.ucargo.android.data.OrderRepository
import mx.ucargo.android.entity.Order
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString

@RunWith(JUnit4::class)
class GetOrderUseCaseImplTest {
    val orderRepository = mock<OrderRepository>()

    lateinit var getOrderUseCase: GetOrderUseCase

    @Before
    fun setUp() {
        getOrderUseCase = GetOrderUseCaseImpl(orderRepository)
    }

    @Test
    fun execute() {
        val expectedOrder = Order()
        val captor = argumentCaptor<(Order) -> Unit>()
        val success = mock<(Order) -> Unit>()

        getOrderUseCase.execute("ANY_ORDER_ID", success, mock())

        verify(orderRepository).findById(anyString(), captor.capture(), any())
        captor.firstValue.invoke(expectedOrder)
        verify(success).invoke(eq(expectedOrder))
    }
}
