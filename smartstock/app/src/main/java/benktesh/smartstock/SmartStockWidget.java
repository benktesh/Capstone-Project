package benktesh.smartstock;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import benktesh.smartstock.Model.Stock;
import benktesh.smartstock.Utils.SmartStockConstant;

/**
 * Implementation of App Widget functionality.
 */
public class SmartStockWidget extends AppWidgetProvider {

    private static final String TAG = SmartStockWidget.class.getSimpleName();

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int[] appWidgetIds, Stock stock) {

        Log.d(TAG, "updatingAppWidget");

        CharSequence widgetText = context.getString(R.string.app_name);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.smart_stock_widget);

        Intent intent = null;
        //if there is no stock, then clicking would open the main else it will open the details
        if (stock != null && stock.Symbol != null) {
            views.setTextViewText(R.id.appwidget_text, widgetText);
            views.setTextViewText(R.id.stock_symbol, stock.Symbol);
            views.setTextViewText(R.id.stock_price, String.valueOf(stock.Price));
            views.setTextViewText(R.id.stock_change, String.valueOf(stock.Change));
            intent = new Intent(context, SearchActivity.class);
            intent.putExtra(SmartStockConstant.ParcelableStock, stock);

        } else {
            intent = new Intent(context, MainActivity.class);
            Log.d(TAG, "Stock is null");
        }

        //open the app form widget
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.appwidget_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        updateAppWidget(context, appWidgetManager, appWidgetIds, new Stock());
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

