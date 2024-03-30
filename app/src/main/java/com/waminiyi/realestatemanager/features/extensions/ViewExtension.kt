package com.waminiyi.realestatemanager.features.extensions

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

fun EditText.afterTextChanged(handleNewValue: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val enteredText = s.toString().trim()
            handleNewValue(enteredText)
        }
    })
}

fun TextView.showAsSelected(isSelected: Boolean, selectedColorRes: Int, nonSelectedColorRes: Int) {
    val textColor = if (isSelected) {
        selectedColorRes
    } else {
        nonSelectedColorRes
    }

    setTextColor(ContextCompat.getColor(context, textColor))
}

fun ImageView.showAsSelected(
    isSelected: Boolean,
    selectedColorRes: Int,
    nonSelectedColorRes: Int
) {
    val color = if (isSelected) {
        selectedColorRes
    } else {
        nonSelectedColorRes
    }
    val tint = ContextCompat.getColor(context, color)
    drawable.setTint(tint)
    DrawableCompat.setTint(DrawableCompat.wrap(background), tint)
}

fun TextInputEditText.updateValue(newValue: String) {
    with(this) {
        setText(newValue)
        setSelection(newValue.length)
    }
}

fun Context.showInformationDialog(information: String) {
    MaterialAlertDialogBuilder(this)
        .setMessage(information)
        .setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}
