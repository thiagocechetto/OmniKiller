package com.casadelfuego.omnikiller.ktx

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class StringExtensionsKtTest {

    @Test
    fun string0ToBoolean() = assertFalse("0".toBoolean())

    @Test
    fun stringEmptyToBoolean() = assertFalse("".toBoolean())

    @Test
    fun stringFalseToBoolean() = assertFalse("false".toBoolean())

    @Test
    fun stringFaLsEToBoolean() = assertFalse("FaLsE".toBoolean())

    @Test
    fun stringSpaceToBoolean() = assertFalse(" ".toBoolean())

    @Test
    fun stringRandomToBoolean() = assertTrue("random".toBoolean())

    @Test
    fun string1ToBoolean() = assertTrue("1".toBoolean())

    @Test
    fun string0IsFalse() = assertTrue("0".isFalse())

    @Test
    fun stringEmptyIsFalse() = assertTrue("".isFalse())

    @Test
    fun stringFalseIsFalse() = assertTrue("false".isFalse())

    @Test
    fun stringFaLsEIsFalse() = assertTrue("FaLsE".isFalse())

    @Test
    fun stringSpaceIsFalse() = assertTrue(" ".isFalse())

    @Test
    fun stringRandomIsFalse() = assertFalse("random".isFalse())

    @Test
    fun string1IsFalse() = assertFalse("1".isFalse())

    @Test
    fun toDate() {
        val date = "2019-05-31".toDate()

        val calendar = Calendar.getInstance()
        calendar.time = date

        assertEquals(2019, calendar.get(Calendar.YEAR))
        assertEquals(5, calendar.get(Calendar.MONTH) + 1)
        assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH))
    }


    @Test
    fun invalidToDate() {
        val date = "2019 05 31".toDate()

        assertNull(date)
    }

    @Test
    fun toDateTime() {
        val date = "2018-05-24 20:34:48".toDateTime()

        val calendar = Calendar.getInstance()
        calendar.time = date

        assertEquals(2018, calendar.get(Calendar.YEAR))
        assertEquals(5, calendar.get(Calendar.MONTH) + 1)
        assertEquals(24, calendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(20, calendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(34, calendar.get(Calendar.MINUTE))
        assertEquals(48, calendar.get(Calendar.SECOND))
    }


    @Test
    fun invalidToDateTime() {
        val date = "2019 05 31 20-34".toDate()

        assertNull(date)
    }
}