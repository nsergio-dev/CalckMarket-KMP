package com.nsergiodev.calckmarket.core.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CurrencyVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text.filter { it.isDigit() }

        val formatted = if (raw.isEmpty()) {
            ""
        } else {
            val value = raw.toDoubleOrNull() ?: 0.0
            value.asCurrency()
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = formatted.length

            override fun transformedToOriginal(offset: Int): Int = raw.length
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}