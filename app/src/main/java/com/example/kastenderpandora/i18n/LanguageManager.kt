package com.example.kastenderpandora.i18n

import android.content.Context

object LanguageManager {

    fun getCurrentLanguage(context: Context): String {
        return context.resources.configuration.locales[0].language
    }

    fun getSupportedLanguages(): List<Language> {
        return listOf(
            Language("de", "Deutsch"),
            Language("en", "English")
        )
    }

    data class Language(val code: String, val displayName: String)
}
