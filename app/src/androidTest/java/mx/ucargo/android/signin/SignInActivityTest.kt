package mx.ucargo.android.signin

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import com.nhaarman.mockito_kotlin.whenever
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import mx.ucargo.android.R
import mx.ucargo.android.app.TestApp
import mx.ucargo.android.data.ApiGateway
import mx.ucargo.android.entity.Account
import mx.ucargo.android.orderlist.OrderListActivity
import mx.ucargo.android.signup.SignUpActivity
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import javax.inject.Inject

class SignInActivityTest {
    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var intentsTestRule = IntentsTestRule(SignInActivity::class.java)

    @Inject
    lateinit var apiGateway: ApiGateway

    @Before
    fun setUp() {
        val testApp = intentsTestRule.activity.applicationContext as TestApp
        testApp.testAppComponent.inject(this)
    }

    @Test
    fun successfulSignIn() {
        whenever(apiGateway.signIn(anyString(), anyString())).thenReturn(Account())

        clickOn(R.id.sendButton)

        assertThat(intentsTestRule.activity.isFinishing, `is`(true))
        intended(hasComponent(OrderListActivity::class.java.name))
    }

    @Test
    fun signUp() {
        clickOn(R.id.signUpButton)

        intended(hasComponent(SignUpActivity::class.java.name))
    }
}
