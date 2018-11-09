package benktesh.smartstock;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import benktesh.smartstock.Model.Stock;
import benktesh.smartstock.Utils.SmartStockConstant;

/**
 * Implementation of App Widget functionality.
 */
public class SmartStockWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Stock stock) {

        CharSequence widgetText = context.getString(R.string.app_name);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.smart_stock_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);


        //open the app form widegt
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        intent.putExtra(SmartStockConstant.CURRENTSTOCK, stock);


        views.setOnClickPendingIntent(R.id.appwidget_layout, pendingIntent);
        // Instruct the widget manager to update the widget


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, new Stock());
        }
        //updateAppWidget(context, appWidgetManager, appWidgetIds, stock);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

