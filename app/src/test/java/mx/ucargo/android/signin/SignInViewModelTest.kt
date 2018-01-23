package mx.ucargo.android.signin

import android.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SignInViewModelTest {
    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()

    lateinit var signInViewModel: SignInViewModel

    @Before
    fun setUp() {
        signInViewModel = SignInViewModel()
    }

    @Test
    fun signIn() {
        signInViewModel.isSignIn.value = false

        signInViewModel.signIn("ANY_USERNAME", "ANY_PASSWORD")

        assertTrue(signInViewModel.isSignIn.value!!)
    }
}
