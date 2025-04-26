package instamovies.app.core.util

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    private val TAG = DateUtils::class.java.simpleName

    fun formatStringDateTime(
        date: String,
        pattern: String,
        outputPattern: String,
    ): String? {
        try {
            val dateFormatter = SimpleDateFormat(pattern, Locale.getDefault())
            val outputFormatter = SimpleDateFormat(outputPattern, Locale.getDefault())

            val parsedDate = dateFormatter.parse(date)
            if (parsedDate != null) {
                return outputFormatter.format(parsedDate)
            }
        } catch (e: NullPointerException) {
            Log.e(TAG, e.message, e)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, e.message, e)
        } catch (e: ParseException) {
            Log.e(TAG, e.message, e)
        }

        return null
    }

    fun formatCalendarDateTime(
        dateTime: Calendar,
        outputPattern: String,
    ): String? {
        try {
            val outputFormatter = SimpleDateFormat(outputPattern, Locale.getDefault())
            return outputFormatter.format(dateTime.time)
        } catch (e: NullPointerException) {
            Log.e(TAG, e.message, e)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, e.message, e)
        }

        return null
    }

    fun formatTimeInMillis(
        timeMillis: Long,
        pattern: String,
    ): String? {
        val calendarDate = Calendar.getInstance()
        calendarDate.timeInMillis = timeMillis
        return formatCalendarDateTime(calendarDate, pattern)
    }

    fun formatDateInMillis(
        dateInMillis: Long?,
        pattern: String,
    ): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(Date(dateInMillis ?: 0))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDate(
        dateString: String,
        pattern: String,
    ): Boolean =
        try {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            formatter.parse(dateString)
            true
        } catch (_: IllegalArgumentException) {
            false
        } catch (_: DateTimeParseException) {
            false
        }
}
