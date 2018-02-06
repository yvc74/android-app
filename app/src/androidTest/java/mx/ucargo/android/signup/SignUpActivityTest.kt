package mx.ucargo.android.signup

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.test.espresso.intent.rule.IntentsTestRule
import com.schibsted.spain.barista.interaction.BaristaClickInteractions
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
        BaristaClickInteractions.clickOn(R.id.sendButton)

        assertTrue(intentsTestRule.getActivity().isFinishing())
    }
}
