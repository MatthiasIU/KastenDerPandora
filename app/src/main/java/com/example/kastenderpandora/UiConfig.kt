package com.example.kastenderpandora

import android.content.Context

object UiConfig {
    private const val PREFS = "ui_prefs"
    private const val KEY_HOME_GRID_COLUMNS = "home_grid_columns"

    fun getHomeGridColumns(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val defaultColumns = context.resources.getInteger(R.integer.home_grid_columns_default)
        return prefs.getInt(KEY_HOME_GRID_COLUMNS, defaultColumns).coerceIn(1, 6)
    }

    fun setHomeGridColumns(context: Context, columns: Int) {
        val safe = columns.coerceIn(1, 6)
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_HOME_GRID_COLUMNS, safe)
            .apply()
    }
}
