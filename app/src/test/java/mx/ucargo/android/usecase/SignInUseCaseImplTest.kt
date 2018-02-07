package mx.ucargo.android.usecase

import com.nhaarman.mockito_kotlin.*
import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.data.UCargoGateway
import mx.ucargo.android.entity.Account
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString

@RunWith(JUnit4::class)
class SignInUseCaseImplTest {
    val uCargoGateway = mock<UCargoGateway>()
    val accountStorage = mock<AccountStorage>()

    lateinit var signInUseCase: SignInUseCase

    @Before
    fun setUp() {
        signInUseCase = SignInUseCaseImpl(uCargoGateway, accountStorage)
    }

    @Test
    fun successfulSignIn() {
        val success = mock<() -> Unit>()
        val captor = argumentCaptor<(Account) -> Unit>()

        signInUseCase.execute("ANY_USERNAME", "ANY_PASSWORD", success, mock())

        verify(uCargoGateway).signIn(anyString(), anyString(), captor.capture(), any())
        captor.firstValue.invoke(Account())
        verify(success).invoke()
    }

    @Test
    fun failureSignIn() {
        val success = mock<() -> Unit>()
        val failure = mock<(Throwable) -> Unit>()
        val captor = argumentCaptor<(Throwable) -> Unit>()

        signInUseCase.execute("ANY_USERNAME", "ANY_PASSWORD", success, failure)

        verify(uCargoGateway).signIn(anyString(), anyString(), any(), captor.capture())
        captor.firstValue.invoke(Throwable())
        verify(failure).invoke(any())
        verifyZeroInteractions(success)
    }
}
