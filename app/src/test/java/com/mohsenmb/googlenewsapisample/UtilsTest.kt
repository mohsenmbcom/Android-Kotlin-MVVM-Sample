package com.mohsenmb.googlenewsapisample

import com.mohsenmb.googlenewsapisample.repository.toLocalDate
import org.junit.Assert
import org.junit.Test
import java.text.ParseException
import java.util.*
import java.util.Calendar.*

class UtilsTest {

    // region Tests of the time formatter
    @Test
    fun stringDates_correctFormatted_isValid() {
        val sampleDate = "2019-08-08T18:25:51Z"
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Istanbul"))

        val expected = Calendar.getInstance(TimeZone.getDefault()).apply {
            set(YEAR, 2019)
            set(MONTH, 7) // Month starts from 0
            set(DAY_OF_MONTH, 8)

            set(HOUR_OF_DAY, 21)
            set(MINUTE, 25)
            set(SECOND, 51)

            set(MILLISECOND, 0)
        }.time

        Assert.assertEquals(expected, sampleDate.toLocalDate())
    }

    @Test
    fun stringDates_malformed_throwException() {
        val sampleDate = "2019-08-08"
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Istanbul"))

        try {
            print(sampleDate.toLocalDate())
        } catch (e: ParseException) {
            Assert.fail("Should not have thrown java.text.ParseException")
        }
    }
    // endregion
}