package mx.ucargo.android.usecase

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import mx.ucargo.android.entity.Order
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class BeginUseCaseImplTest {

    lateinit var beginUseCase: BeginUseCase

    @Before
    fun setUp() {
        beginUseCase = BeginUseCaseImpl()
    }

    @Test
    fun existingAccount() {
        val success = mock<(Order.Status) -> Unit>()

        beginUseCase.execute("ANY_ORDER_ID", success, mock())

        verify(success).invoke(eq(Order.Status.CUSTOMS))
    }
}
