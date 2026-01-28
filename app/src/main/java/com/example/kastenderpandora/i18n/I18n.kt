package com.example.kastenderpandora.i18n

import android.content.Context
import androidx.annotation.StringRes

object I18n {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context.applicationContext
    }

    fun string(@StringRes resId: Int): String {
        return applicationContext.getString(resId)
    }

    fun string(@StringRes resId: Int, vararg formatArgs: Any): String {
        return applicationContext.getString(resId, *formatArgs)
    }

}
