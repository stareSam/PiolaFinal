package com.example.piolafinal

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class WidgetPiola : AppWidgetProvider()
{
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }// onUpdate

    override fun onEnabled(context: Context)
    {
        // Enter relevant functionality for when the first widget is created
    }// onEnabled

    override fun onDisabled(context: Context)
    {
        // Enter relevant functionality for when the last widget is disabled
    }// onDisabled
} //Class Widgetpiola

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Construye las remoteviews
    val views = RemoteViews(context.packageName, R.layout.widget_piola)

    val Cantidad = Count(context) //Toma la cantidad de libros
    views.setTextViewText(R.id.appwidget_text, Cantidad.toString()) //y se la pone al

    appWidgetManager.updateAppWidget(appWidgetId, views)//Actualiza
}// updateAppWidget

//Consigue el número de registros
fun Count(context: Context) : Int
{
    var libreBDHelper= BdLibreriaHelper(context)
    val db: SQLiteDatabase = libreBDHelper.readableDatabase
    val cursor = db.rawQuery("SELECT * FROM Libreria", null)
    val Contar = cursor.count
    db.close()
    return Contar
}