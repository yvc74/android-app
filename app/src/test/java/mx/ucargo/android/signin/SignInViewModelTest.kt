package mx.ucargo.android.signin

import android.arch.core.executor.testing.InstantTaskExecutorRule
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

    @Test
    fun setUp() {
        signInViewModel = SignInViewModel()
    }
}
