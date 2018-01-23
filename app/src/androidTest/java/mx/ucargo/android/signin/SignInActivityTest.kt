package mx.ucargo.android.signin

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.withId
import mx.ucargo.android.R
import mx.ucargo.android.bidding.BiddingActivity
import mx.ucargo.android.signup.SignUpActivity
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignInActivityTest {
    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var intentsTestRule = IntentsTestRule(SignInActivity::class.java)

    @Before
    fun setUp() {
    }

    @Test
    fun signIn() {
        onView(withId(R.id.signInButton)).perform(click())

        assertTrue(intentsTestRule.getActivity().isFinishing())
        intended(hasComponent(BiddingActivity::class.java!!.getName()))
    }

    @Test
    fun signUp() {
        onView(withId(R.id.signUpButton)).perform(click())

        intended(hasComponent(SignUpActivity::class.java!!.getName()))
    }
}
