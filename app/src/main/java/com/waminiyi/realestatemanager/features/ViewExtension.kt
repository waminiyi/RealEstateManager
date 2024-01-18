package com.waminiyi.realestatemanager.features

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.waminiyi.realestatemanager.R

fun TextView.setAgentSelection(isSelected: Boolean) {
    if (isSelected) {
        setTextColor(ContextCompat.getColor(context, R.color.selectedAgentColor))
    } else {
        setTextColor(ContextCompat.getColor(context, R.color.nonSelectedAgentColor))
    }
}

fun ImageView.setAgentSelection(isSelected: Boolean) {
    if (isSelected) {
        setBackgroundResource(R.drawable.rounded_border)
    } else {
        setBackgroundResource(android.R.color.transparent)
    }
}