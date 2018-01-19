package mx.ucargo.android.signin

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

class SignInActivityTest {
    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(SignInActivity::class.java)

    @Test
    fun setUp() {
    }
}
