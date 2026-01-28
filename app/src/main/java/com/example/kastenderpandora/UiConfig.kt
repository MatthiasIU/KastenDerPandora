package com.example.kastenderpandora

import android.content.Context

object UiConfig {

    private const val MIN_COLUMNS = 2
    private const val MAX_COLUMNS = 6

    // Optional: alte Keys (wenn du schon Releases mit dem alten UiConfig hattest)
    private const val OLD_PREFS = "ui_prefs"
    private const val OLD_KEY_HOME_GRID_COLUMNS = "home_grid_columns"

    fun getHomeGridColumns(context: Context): Int {
        val appPrefs = context.getSharedPreferences(PandoraApplication.PREFS_NAME, Context.MODE_PRIVATE)
        val defaultColumns = PandoraApplication.DEFAULT_GRID_COLUMNS

        // Neu (Quelle der Wahrheit)
        if (appPrefs.contains(PandoraApplication.KEY_GRID_COLUMNS)) {
            return appPrefs.getInt(PandoraApplication.KEY_GRID_COLUMNS, defaultColumns)
                .coerceIn(MIN_COLUMNS, MAX_COLUMNS)
        }

        // Optional: Migration von alt -> neu
        val oldPrefs = context.getSharedPreferences(OLD_PREFS, Context.MODE_PRIVATE)
        val migrated = oldPrefs.getInt(
            OLD_KEY_HOME_GRID_COLUMNS,
            context.resources.getInteger(R.integer.home_grid_columns_default)
        ).coerceIn(MIN_COLUMNS, MAX_COLUMNS)

        appPrefs.edit().putInt(PandoraApplication.KEY_GRID_COLUMNS, migrated).apply()
        return migrated
    }

    fun setHomeGridColumns(context: Context, columns: Int) {
        val safe = columns.coerceIn(MIN_COLUMNS, MAX_COLUMNS)
        context.getSharedPreferences(PandoraApplication.PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt(PandoraApplication.KEY_GRID_COLUMNS, safe)
            .apply()
    }
}
