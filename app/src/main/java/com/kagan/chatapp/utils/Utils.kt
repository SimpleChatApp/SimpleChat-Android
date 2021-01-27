package com.kagan.chatapp.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import com.kagan.chatapp.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showApiFailure(context: Context, view: View) {
        Snackbar.make(
            view,
            context.resources.getString(R.string.api_failure),
            Snackbar.LENGTH_SHORT
        )
            .show()
    }

    fun formatTime(date: String, context: Context): String? {
        val dateObject = mapToDate(date)
        return if (is24HourFormat(context)) {
            SimpleDateFormat("H:mm", Locale.UK).format(dateObject!!)
        } else {
            SimpleDateFormat("h:mm a", Locale.UK).format(dateObject!!)
        }
    }

    fun formatDate(date: String): String? {
        val dateObject = mapToDate(date)
        return SimpleDateFormat("EEE, MMM d", Locale.UK).format(dateObject!!)
    }

    private fun is24HourFormat(context: Context): Boolean =
        android.text.format.DateFormat.is24HourFormat(context)

    private fun mapToDate(date: String): Date? {
        try {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.UK)
            return simpleDateFormat.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }
}