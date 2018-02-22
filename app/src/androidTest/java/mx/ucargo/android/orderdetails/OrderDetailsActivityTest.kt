package mx.ucargo.android.orderdetails

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.swipeUp
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.whenever
import com.schibsted.spain.barista.assertion.BaristaEnabledAssertions.assertDisabled
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import mx.ucargo.android.R
import mx.ucargo.android.app.TestApp
import mx.ucargo.android.data.ApiGateway
import mx.ucargo.android.entity.Order
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import javax.inject.Inject

private const val ANY_ORDER_ID = "ANY_ORDER_ID"
private const val ANY_INT = "2000"

class OrderDetailsActivityTest {
    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(OrderDetailsActivity::class.java, false, false)

    private val intent = createIntent()

    @Inject
    lateinit var apiGateway: ApiGateway

    private fun createIntent(): Intent {
        val intent = Intent()
        intent.putExtra(OrderDetailsActivity.ORDER_ID, ANY_ORDER_ID)

        return intent
    }

    @Before
    fun setUp() {
        val testApp = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApp
        testApp.testAppComponent.inject(this)
        reset(apiGateway)
    }

    @Test
    fun sendQuote() {
        whenever(apiGateway.findById(anyString())).thenReturn(Order(id = ANY_ORDER_ID, status = Order.Status.NEW))
        whenever(apiGateway.sendQuote(any())).thenReturn(Order(id = ANY_ORDER_ID, status = Order.Status.SENT_QUOTE))

        activityTestRule.launchActivity(intent)
        onView(withId(android.R.id.content)).perform(swipeUp())
        writeTo(R.id.quoteEditText, ANY_INT);
        clickOn(R.id.sendButton)

        assertDisabled(R.id.quoteEditText)
        assertDisplayed(R.id.cancelQuoteButton)
    }

    @Test
    fun approvedQuote() {
        whenever(apiGateway.findById(anyString())).thenReturn(Order(id = ANY_ORDER_ID, status = Order.Status.APPROVED))

        activityTestRule.launchActivity(intent)
        onView(withId(android.R.id.content)).perform(swipeUp())

        assertDisplayed(R.id.beginButton)
    }
}
