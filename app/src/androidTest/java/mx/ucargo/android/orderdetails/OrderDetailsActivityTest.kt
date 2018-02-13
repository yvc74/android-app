package mx.ucargo.android.orderdetails

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.swipeUp
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.schibsted.spain.barista.assertion.BaristaEnabledAssertions.assertDisabled
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import mx.ucargo.android.R
import mx.ucargo.android.app.TestApp
import mx.ucargo.android.data.OrderRepository
import mx.ucargo.android.entity.Order
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import javax.inject.Inject

class OrderDetailsActivityTest {
    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(OrderDetailsActivity::class.java, false, false)

    @Inject
    lateinit var orderRepository: OrderRepository

    private val ANY_ORDER_ID = "ANY_ORDER_ID"

    @Before
    fun setUp() {
        val intent = Intent()
        intent.putExtra(OrderDetailsActivity.ORDER_ID, ANY_ORDER_ID)
        activityTestRule.launchActivity(intent)

        val testApp = activityTestRule.activity.applicationContext as TestApp
        testApp.testAppComponent.inject(this)
    }

    @Test
    fun sendQuote() {
        val ANY_INT = "2000"
        val captor = argumentCaptor<() -> Unit>()
        val repositoryCaptor = argumentCaptor<(Order) -> Unit>()
        verify(orderRepository).findById(anyString(), repositoryCaptor.capture(), any())
        repositoryCaptor.firstValue.invoke(Order(id = ANY_ORDER_ID, status = Order.Status.NEW))

        onView(withId(android.R.id.content)).perform(swipeUp())
        writeTo(R.id.quoteEditText, ANY_INT);
        clickOn(R.id.sendButton)

        verify(orderRepository).persistQuote(any(), anyString(), captor.capture(), any())
        captor.firstValue.invoke()
        assertDisabled(R.id.quoteEditText)
        assertDisplayed(R.id.cancelQuoteButton)
    }

    @Test
    fun approvedQuote() {
        val repositoryCaptor = argumentCaptor<(Order) -> Unit>()
        verify(orderRepository).findById(anyString(), repositoryCaptor.capture(), any())
        repositoryCaptor.firstValue.invoke(Order(id = ANY_ORDER_ID, status = Order.Status.APPROVED))

        onView(withId(android.R.id.content)).perform(swipeUp())

        assertDisplayed(R.id.beginButton)
    }

    @After
    fun tearDown() {
        reset(orderRepository)
    }
}
