package mx.ucargo.android.usecase

import com.nhaarman.mockito_kotlin.whenever
import mx.ucargo.android.data.ApiGateway
import mx.ucargo.android.entity.Order
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetOrderUseCaseImplTest {
    @Mock
    lateinit var orderRepository: ApiGateway

    lateinit var getOrderUseCase: GetOrderUseCaseImpl

    @Before
    fun setUp() {
        getOrderUseCase = GetOrderUseCaseImpl(orderRepository)
    }

    @Test
    fun execute() {
        val expectedOrder = Order()

        whenever(orderRepository.findById(anyString())).thenReturn(expectedOrder)

        val order = getOrderUseCase.executeSync("ANY_ORDER_ID")

        assertThat(order, `is`(equalTo(expectedOrder)))
    }
}
