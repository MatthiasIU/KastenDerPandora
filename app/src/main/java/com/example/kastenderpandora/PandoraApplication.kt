package com.example.kastenderpandora

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale
import com.example.kastenderpandora.i18n.I18n

class PandoraApplication : Application() {

    companion object {
        const val PREFS_NAME = "AppPrefs"
        const val KEY_LANGUAGE = "language"
        const val KEY_DARKMODE = "darkmode"
        const val KEY_GRID_COLUMNS = "home_grid_columns_default"
        const val DEFAULT_GRID_COLUMNS = 4
        var currentLocale: Locale? = null
    }

    override fun onCreate() {
        super.onCreate()

        // Darkmode anwenden (AppCompatDelegate ist der Standardweg für Views)
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val darkMode = prefs.getBoolean(KEY_DARKMODE, false)

        AppCompatDelegate.setDefaultNightMode(
            if (darkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        I18n.init(this)
    }

    override fun attachBaseContext(base: android.content.Context?) {
        val prefs = base?.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val savedLanguage = prefs?.getString(KEY_LANGUAGE, null)

        android.util.Log.d("PandoraApplication", "Saved language from prefs: $savedLanguage")

        if (savedLanguage != null) {
            android.util.Log.d("PandoraApplication", "Applying language: $savedLanguage")

            val locale = Locale.forLanguageTag(savedLanguage)
            Locale.setDefault(locale)
            currentLocale = locale

            val config = Configuration()
            config.setLocale(locale)
            config.setLayoutDirection(locale)

            super.attachBaseContext(base.createConfigurationContext(config))
        } else {
            android.util.Log.d("PandoraApplication", "No saved language, using default")
            super.attachBaseContext(base)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        android.util.Log.d("PandoraApplication", "Configuration changed")
    }
}
