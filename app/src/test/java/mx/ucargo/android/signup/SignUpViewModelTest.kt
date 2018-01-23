package mx.ucargo.android.signup

import android.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SignUpViewModelTest {
    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()

    lateinit var signUpViewModel: SignUpViewModel

    @Before
    fun setUp() {
        signUpViewModel = SignUpViewModel()
    }

    @Test
    fun signUp() {
        signUpViewModel.isSignUp.value = false

        signUpViewModel.signUp("ANY_USER_NAME", "ANY_PASSWORD")

        Assert.assertTrue(signUpViewModel.isSignUp.value!!)
    }
}
