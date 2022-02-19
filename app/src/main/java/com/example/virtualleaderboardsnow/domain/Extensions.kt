package com.example.virtualleaderboardsnow.domain

import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import android.text.TextUtils
import android.util.Patterns
import android.util.TypedValue
import android.widget.EditText
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.widget.doAfterTextChanged
import com.example.virtualleaderboardsnow.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
val EditText.textFlow: Flow<String>
    get() = callbackFlow {
        val textWatcher = doAfterTextChanged { trySend(it.toString()) }
        awaitClose {
            removeTextChangedListener(textWatcher)
            cancel()
        }
    }

@ColorInt
fun Context.themeColor(@AttrRes attrRes: Int): Int = TypedValue()
    .apply { theme.resolveAttribute(attrRes, this, true) }
    .data

fun Context.showDialog(title: String, onOkClick: (value: String) -> Unit) {
    val builder = MaterialAlertDialogBuilder(
        this, R.style.DialogTheme
    )
    builder.setTitle(title)

    // Set up the input
    val input = EditText(this)
    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
    input.inputType = InputType.TYPE_CLASS_TEXT
    builder.setView(input)
        .setPositiveButton("OK") { _: DialogInterface?, _: Int -> onOkClick(input.text.toString()) }
        .setNegativeButton("Cancel") { dialog: DialogInterface, which: Int -> dialog.cancel() }
    builder.show()
}

@ExperimentalCoroutinesApi
val SearchView.textFlow: Flow<String>
    get() = callbackFlow {
        setOnQueryTextListener(object : SearchView.OnCloseListener,
            SearchView.OnQueryTextListener {
            override fun onClose(): Boolean {

                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                trySend(p0 ?: "")

                return true
            }
        })
        awaitClose {
            cancel()
        }
    }

fun String.isValidEmail() =
    isEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()