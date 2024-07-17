package com.example.cinevote.screens.details

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.compose.material3.Text
import com.example.cinevote.R
import com.example.cinevote.util.TMDBService
import java.util.Calendar
import kotlin.random.Random

/**
 * Implementation of App Widget functionality.
 */
class NewAppWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        setDailyUpdate(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        cancelDailyUpdate(context)
    }


}
/*
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val tmdb = TMDBService()
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)

    // Aggiorna con il testo predefinito
    val widgetText = context.getString(R.string.appwidget_text)
    views.setTextViewText(R.id.appwidget_text, widgetText)
    views.setTextViewText(R.id.appwidget_text2, widgetText) // Aggiungi testo predefinito per altri TextView
    views.setTextViewText(R.id.appwidget_text3, widgetText) // Aggiungi testo predefinito per altri TextView
    appWidgetManager.updateAppWidget(appWidgetId, views)

    // Fetch dei dati del film
    tmdb.fetchFilmData(
        url = "https://api.themoviedb.org/3/discover/movie?include_adult=true&include_video=false&language=it&page=1&primary_release_date.gte=2024-04-17&region=it&sort_by=popularity.desc&with_release_type=3",
        onFailure = {
            views.setTextViewText(R.id.appwidget_text, "Errore di caricamento")
            appWidgetManager.updateAppWidget(appWidgetId, views)
        },
        onSuccess = { films ->
            val topFilms = films.take(3)
            if (topFilms.isNotEmpty()) views.setTextViewText(R.id.appwidget_text, topFilms[0].title)
            if (topFilms.size > 1) views.setTextViewText(R.id.appwidget_text2, topFilms[1].title)
            //if (topFilms.size > 2) views.setTextViewText(R.id.appwidget_text3, topFilms[2].title)

            // Aggiorna il widget con i nuovi testi
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    )
}*/

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val tmdb = TMDBService()
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)

    // Imposta il testo predefinito mentre i dati vengono caricati
    val widgetText = context.getString(R.string.appwidget_text)
    views.setTextViewText(R.id.appwidget_text, "Film del giorno")
    views.setTextViewText(R.id.appwidget_text2, widgetText)
    views.setTextViewText(R.id.appwidget_text3, widgetText)
    appWidgetManager.updateAppWidget(appWidgetId, views)

    // Fetch dei dati del film
    tmdb.fetchFilmData(
        url = "https://api.themoviedb.org/3/discover/movie?include_adult=true&include_video=false&language=it&page=1&primary_release_date.gte=2024-04-17&region=it&sort_by=popularity.desc&with_release_type=3",
        onFailure = {
            views.setTextViewText(R.id.appwidget_text, "Errore di caricamento")
            appWidgetManager.updateAppWidget(appWidgetId, views)
        },
        onSuccess = { films ->
            /*if (films.isNotEmpty()) {
                // Seleziona un film a caso dalla lista dei film
                val randomFilm = films[Random.nextInt(films.size)]
                views.setTextViewText(R.id.appwidget_text, randomFilm.title)

                // Aggiorna il widget con il titolo del film selezionato
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }*/
            views.setTextViewText(R.id.appwidget_text2, "Film consigliati")

            val topFilms = films.take(3)
            if (topFilms.isNotEmpty()) views.setTextViewText(R.id.appwidget_text2, topFilms[0].title)
            if (topFilms.size > 1) views.setTextViewText(R.id.appwidget_text3, topFilms[1].title)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    )
}



private fun setDailyUpdate(context: Context) {
    val intent = Intent(context, NewAppWidget::class.java).apply {
        action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
    }
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    alarmManager.setInexactRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        AlarmManager.INTERVAL_DAY,
        pendingIntent
    )
}

private fun cancelDailyUpdate(context: Context) {
    val intent = Intent(context, NewAppWidget::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
}


