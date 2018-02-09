package mx.ucargo.android.orderdetails

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class MappersTest {
    @Before
    fun setUp() {
    }

    @Test
    fun sameDay() {
        val calendar = Calendar.getInstance()
        calendar.set(2018, 1, 9)

        val dateDiff = Mappers.daysHoursDiff(Date(calendar.timeInMillis), Date(calendar.timeInMillis))

        assertEquals(Pair(0, 0), dateDiff)
    }

    @Test
    fun oneDay() {
        val calendar = Calendar.getInstance()
        calendar.set(2018, 1, 9)
        val start = Date(calendar.timeInMillis)
        calendar.set(2018, 1, 10)
        val end = Date(calendar.timeInMillis)

        val dateDiff = Mappers.daysHoursDiff(start, end)

        assertEquals(Pair(1, 0), dateDiff)
    }

    @Test
    fun oneWeek() {
        val calendar = Calendar.getInstance()
        calendar.set(2018, 1, 9)
        val start = calendar.time
        calendar.set(2018, 1, 16)
        val end = calendar.time

        val dateDiff = Mappers.daysHoursDiff(start, end)

        assertEquals(Pair(7, 0), dateDiff)
    }

    @Test
    fun oneYear() {
        val calendar = Calendar.getInstance()
        calendar.set(2018, 1, 9)
        val start = Date(calendar.timeInMillis)
        calendar.set(2019, 1, 9)
        val end = Date(calendar.timeInMillis)

        val dateDiff = Mappers.daysHoursDiff(start, end)

        assertEquals(Pair(365, 0), dateDiff)
    }

    @Test
    fun oneLeapYear() {
        val calendar = Calendar.getInstance()
        calendar.set(2020, 0, 1)
        val start = Date(calendar.timeInMillis)
        calendar.set(2021, 0, 1)
        val end = Date(calendar.timeInMillis)

        val dateDiff = Mappers.daysHoursDiff(start, end)

        assertEquals(Pair(366, 0), dateDiff)
    }

    @Test
    fun oneDaysNoLeapYear() {
        val calendar = Calendar.getInstance()
        calendar.set(2018, 1, 28)
        val start = Date(calendar.timeInMillis)
        calendar.set(2018, 2, 1)
        val end = Date(calendar.timeInMillis)

        val dateDiff = Mappers.daysHoursDiff(start, end)

        assertEquals(Pair(1, 0), dateDiff)
    }

    @Test
    fun twoDaysLeapYear() {
        val calendar = Calendar.getInstance()
        calendar.set(2020, 1, 28)
        val start = Date(calendar.timeInMillis)
        calendar.set(2020, 2, 1)
        val end = Date(calendar.timeInMillis)

        val dateDiff = Mappers.daysHoursDiff(start, end)

        assertEquals(Pair(2, 0), dateDiff)
    }

    @Test
    fun sameHour() {
        val calendar = Calendar.getInstance()
        calendar.set(2018, 1, 9, 11, 58, 0)

        val dateDiff = Mappers.daysHoursDiff(Date(calendar.timeInMillis), Date(calendar.timeInMillis))

        assertEquals(Pair(0, 0), dateDiff)
    }

    @Test
    fun oneHour() {
        val calendar = Calendar.getInstance()
        calendar.set(2018, 1, 9, 11, 58, 0)
        val start = Date(calendar.timeInMillis)
        calendar.set(2018, 1, 9, 12, 58, 0)
        val end = Date(calendar.timeInMillis)

        val dateDiff = Mappers.daysHoursDiff(start, end)

        assertEquals(Pair(0, 1), dateDiff)
    }

    @Test
    fun twentyThreeHours() {
        val calendar = Calendar.getInstance()
        calendar.set(2018, 1, 9, 11, 58, 0)
        val start = Date(calendar.timeInMillis)
        calendar.set(2018, 1, 10, 10, 58, 0)
        val end = Date(calendar.timeInMillis)

        val dateDiff = Mappers.daysHoursDiff(start, end)

        assertEquals(Pair(0, 23), dateDiff)
    }

    @Test
    fun oneDayOneHour() {
        val calendar = Calendar.getInstance()
        calendar.set(2018, 1, 9, 11, 58, 0)
        val start = Date(calendar.timeInMillis)
        calendar.set(2018, 1, 10, 12, 58, 0)
        val end = Date(calendar.timeInMillis)

        val dateDiff = Mappers.daysHoursDiff(start, end)

        assertEquals(Pair(1, 1), dateDiff)
    }

    @Test
    fun oneDayTwentyThreeHours() {
        val calendar = Calendar.getInstance()
        calendar.set(2018, 1, 9, 11, 58, 0)
        val start = Date(calendar.timeInMillis)
        calendar.set(2018, 1, 11, 10, 58, 0)
        val end = Date(calendar.timeInMillis)

        val dateDiff = Mappers.daysHoursDiff(start, end)

        assertEquals(Pair(1, 23), dateDiff)
    }
}
