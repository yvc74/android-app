package mx.ucargo.android.signup

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers
import mx.ucargo.android.R
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SignUpActivityTest {
    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var intentsTestRule = IntentsTestRule(SignUpActivity::class.java)

    @Test
    fun signUp() {
        Espresso.onView(ViewMatchers.withId(R.id.sendButton)).perform(ViewActions.click())

        assertTrue(intentsTestRule.getActivity().isFinishing())
    }
}
