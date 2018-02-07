package mx.ucargo.android.signin

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import mx.ucargo.android.usecase.SignInUseCase
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString

@RunWith(JUnit4::class)
class SignInViewModelTest {
    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()

    val signInUseCase = mock<SignInUseCase>()

    lateinit var signInViewModel: SignInViewModel

    @Before
    fun setUp() {
        signInViewModel = SignInViewModel(signInUseCase)
    }

    @Test
    fun successfulSignIn() {
        val captor = argumentCaptor<() -> Unit>()
        signInViewModel.isSignIn.value = false

        signInViewModel.send("ANY_USERNAME", "ANY_PASSWORD")

        verify(signInUseCase).execute(anyString(), anyString(), captor.capture(), any())
        captor.firstValue.invoke()
        assertTrue(signInViewModel.isSignIn.value!!)
    }

    @Test
    fun failureSignIn() {
        val captor = argumentCaptor<(Throwable) -> Unit>()

        signInViewModel.send("ANY_USERNAME", "ANY_PASSWORD")

        verify(signInUseCase).execute(anyString(), anyString(), any(), captor.capture())
        captor.firstValue.invoke(Throwable("ANY_ERROR"))
        assertNotNull(signInViewModel.formError.value)
    }
}
