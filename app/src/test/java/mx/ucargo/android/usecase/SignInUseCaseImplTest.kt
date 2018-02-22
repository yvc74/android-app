package mx.ucargo.android.usecase

import com.nhaarman.mockito_kotlin.whenever
import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.data.ApiGateway
import mx.ucargo.android.entity.Account
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SignInUseCaseImplTest {
    @Mock
    lateinit var apiGateway: ApiGateway

    @Mock
    lateinit var accountStorage: AccountStorage

    lateinit var signInUseCase: SignInUseCaseImpl

    @Before
    fun setUp() {
        signInUseCase = SignInUseCaseImpl(apiGateway, accountStorage)
    }

    @Test
    fun successfulSignIn() {
        whenever(apiGateway.signIn(anyString(), anyString())).thenReturn(Account())

        val account = signInUseCase.executeSync("ANY_USERNAME", "ANY_PASSWORD")

        assertThat(account, instanceOf(Account::class.java))
    }

    @Test(expected = Exception::class)
    fun failureSignIn() {
        whenever(apiGateway.signIn(anyString(), anyString())).thenThrow(Exception())

        val order = signInUseCase.executeSync("ANY_USERNAME", "ANY_PASSWORD")

        // Assert Expected Exception
    }
}
