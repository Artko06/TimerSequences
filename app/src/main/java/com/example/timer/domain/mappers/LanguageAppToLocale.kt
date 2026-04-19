package com.example.timer.domain.mappers

import com.example.timer.domain.model.LanguageApp
import java.util.Locale

fun LanguageApp.toLocale(): Locale = when (this) {
    LanguageApp.SYSTEM -> Locale.getDefault()
    LanguageApp.ENGLISH -> Locale.forLanguageTag("en")
    LanguageApp.RUSSIAN -> Locale.forLanguageTag("ru")
    LanguageApp.BELARUSIAN -> Locale.forLanguageTag("be")
}