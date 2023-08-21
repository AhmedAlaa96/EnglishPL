package com.ahmed.english_pl.utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.TextView
import com.ahmed.english_pl.data.models.MatchesFilterData
import com.ahmed.english_pl.data.models.dto.Match
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateTimeHelper {
    private const val DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private const val DATE_FORMAT_YYYY_MM_DD_T_ZEROS = "yyyy-MM-dd'T'00:00:00'Z'"
    private const val DATE_FORMAT_d_MMMM_YYYY = "d MMMM yyyy"
    const val DATE_FORMAT_E_d_MMM = "E d, MMM"
    private const val DATE_FORMAT_HH_MM = "HH:mm"

    fun convertDateStringToAnotherFormat(
        dateString: String?,
        dateParserFormat: String = DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z,
        dateFormatter: String = DATE_FORMAT_E_d_MMM,
        desiredLocale: Locale = Locale.getDefault(),
        alternateValue: String? = Constants.General.EMPTY_TEXT
    ): String? {
        if (dateString.isNullOrEmpty()) {
            return alternateValue
        }
        val parser = SimpleDateFormat(dateParserFormat, desiredLocale)
        val formatter = SimpleDateFormat(dateFormatter, desiredLocale)
        try {
            return formatter.format(parser.parse(dateString)!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return alternateValue
    }


    fun getTimeFormat(
        timeFormatter: String = DATE_FORMAT_HH_MM,
        locale: Locale = Locale.getDefault()
    ): String {
        return if (locale == Locale.ENGLISH)
            timeFormatter
        else
            timeFormatter.reversed()
    }

    fun convertToDate(dateString: String?): Date? {
        return if (dateString.isNullOrEmpty()) null
        else {
            val dateFormat =
                SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z, Locale.getDefault())
            dateFormat.parse(dateString)
        }
    }

    fun isDateTodayOrTmw(dateString: String?): Boolean {
        val date = convertToDate(dateString)
        return if (date == null) false
        else {
            val calendar = Calendar.getInstance()
            calendar.time = date
            val today = Calendar.getInstance()
            val tomorrow = Calendar.getInstance()
            tomorrow.add(Calendar.DATE, 1)

            return (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
                    ) || (calendar.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR) &&
                    calendar.get(Calendar.DAY_OF_YEAR) == tomorrow.get(Calendar.DAY_OF_YEAR)
                    )
        }
    }

    fun getTodayTmwDate(dateString: String?): String {
        val date = convertToDate(dateString)
        return if (date == null) "-"
        else {
            val calendar = Calendar.getInstance()
            calendar.time = date
            val today = Calendar.getInstance()
            val tomorrow = Calendar.getInstance()
            tomorrow.add(Calendar.DATE, 1)
            if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
            ) {
                "Today"
            } else if (calendar.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == tomorrow.get(Calendar.DAY_OF_YEAR)
            ) {
                "Tomorrow"
            } else {
                convertDateStringToAnotherFormat(dateString).alternate()
            }
        }
    }

    fun openDatePickerDialog(
        mContext: Context,
        textView: TextView,
        onOKButtonClicked: (date: String?) -> Unit
    ) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            mContext,
            { view: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                // Update the calendar with the selected date
                calendar.set(year, month, dayOfMonth)

                // Format the date as a string and set it on the button
                val dateFormatterToShow = SimpleDateFormat(DATE_FORMAT_E_d_MMM, Locale.getDefault())
                val dateFormatterToSend =
                    SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_ZEROS, Locale.getDefault())
                textView.text = dateFormatterToShow.format(calendar.time)

                onOKButtonClicked(dateFormatterToSend.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    fun isFromToDateCorrect(fromDate: String, toDate: String): Boolean {
        return convertToDate(toDate)?.after(convertToDate(fromDate)) == true || isSameDayOfYear(
            convertToDate(toDate)!!,
            convertToDate(fromDate)!!
        )
    }

    fun checkDate(dateString: String?): String {
        val date = convertToDate(dateString) ?: Date()
        val calendarToday = Calendar.getInstance()
        val calendarDate = Calendar.getInstance()
        calendarDate.time = date

        if (calendarToday.get(Calendar.YEAR) == calendarDate.get(Calendar.YEAR)
            && calendarToday.get(Calendar.DAY_OF_YEAR) == calendarDate.get(Calendar.DAY_OF_YEAR)
        ) {
            return "Today"
        }

        calendarToday.add(Calendar.DAY_OF_YEAR, 1)
        if (calendarToday.get(Calendar.YEAR) == calendarDate.get(Calendar.YEAR)
            && calendarToday.get(Calendar.DAY_OF_YEAR) == calendarDate.get(Calendar.DAY_OF_YEAR)
        ) {
            return "Tomorrow"
        }

        calendarToday.add(Calendar.DAY_OF_YEAR, -2)
        if (calendarToday.get(Calendar.YEAR) == calendarDate.get(Calendar.YEAR)
            && calendarToday.get(Calendar.DAY_OF_YEAR) == calendarDate.get(Calendar.DAY_OF_YEAR)
        ) {
            return "Yesterday"
        }

        return convertDateStringToAnotherFormat(
            dateString,
            dateFormatter = DATE_FORMAT_E_d_MMM
        ).alternate()
    }

    fun inFilterData(match: Match, matchesFilterData: MatchesFilterData): Boolean {
        return if (matchesFilterData.status != null && matchesFilterData.fromDate != null && matchesFilterData.toDate != null) {
            (match.status == matchesFilterData.status)
                    && betweenDates(match, matchesFilterData.fromDate, matchesFilterData.toDate)
        } else if ((matchesFilterData.status != null && matchesFilterData.fromDate == null && matchesFilterData.toDate == null)) {
            match.status == matchesFilterData.status
        } else {
            (matchesFilterData.status != null && match.status == matchesFilterData.status)
                    || betweenDates(match, matchesFilterData.fromDate, matchesFilterData.toDate)
        }
    }

    private fun betweenDates(
        match: Match,
        fromDateString: String?,
        toDateString: String?
    ): Boolean {
        val matchDate = convertToDate(match.utcDate) ?: Date()
        return if (!fromDateString.isNullOrEmpty() && !toDateString.isNullOrEmpty()) {
            val fromDate = convertToDate(fromDateString) ?: Date()
            val toDate = convertToDate(toDateString) ?: Date()
            isDateInBetween(fromDate, toDate, matchDate)
        } else if (!fromDateString.isNullOrEmpty() && toDateString.isNullOrEmpty()) {
            val fromDate = convertToDate(fromDateString) ?: Date()
            matchDate.after(fromDate) || isSameDayOfYear(matchDate, fromDate)
        } else if (fromDateString.isNullOrEmpty() && !toDateString.isNullOrEmpty()) {
            val toDate = convertToDate(toDateString) ?: Date()
            matchDate.before(toDate) || isSameDayOfYear(matchDate, toDate)
        } else {
            true
        }
    }

    private fun isDateInBetween(startDate: Date, endDate: Date, dateToCheck: Date): Boolean {
        return ((dateToCheck.after(startDate) || isSameDayOfYear(dateToCheck, startDate))
                && (dateToCheck.before(endDate) || isSameDayOfYear(dateToCheck, endDate)))
    }

    private fun isSameDayOfYear(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance()
        cal1.time = date1

        val cal2 = Calendar.getInstance()
        cal2.time = date2

        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
    }
}