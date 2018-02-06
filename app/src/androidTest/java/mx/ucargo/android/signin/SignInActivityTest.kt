package mx.ucargo.android.signin

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import mx.ucargo.android.R
import mx.ucargo.android.bidding.BiddingActivity
import mx.ucargo.android.signup.SignUpActivity
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SignInActivityTest {
    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var intentsTestRule = IntentsTestRule(SignInActivity::class.java)

    @Test
    fun signIn() {
        clickOn(R.id.sendButton)

        assertTrue(intentsTestRule.getActivity().isFinishing())
        intended(hasComponent(BiddingActivity::class.java!!.getName()))
    }

    @Test
    fun signUp() {
        clickOn(R.id.signUpButton)

        intended(hasComponent(SignUpActivity::class.java!!.getName()))
    }
}
