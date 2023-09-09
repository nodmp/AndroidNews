package com.prsixe.anews.glance


import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Implementation of App Widget functionality.
 */
class AndroidNewsGlanceAppWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = AndroidNewsGlanceAppWidget()

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        CoroutineScope(Dispatchers.IO).launch {
            AndroidNesRepo.updateArticles()
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        CoroutineScope(Dispatchers.IO).launch {
            AndroidNesRepo.updateArticles()
        }

    }
}

