package benktesh.smartstock.Utils;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import benktesh.smartstock.Model.Chart;
import benktesh.smartstock.Model.Stock;
import benktesh.smartstock.Model.Symbol;
import benktesh.smartstock.Model.Trade;


@SuppressWarnings("SpellCheckingInspection")
class JsonUtilities {

    private static final String TAG = JsonUtilities.class.getSimpleName();


    public static ArrayList<Symbol> parseSymbol(String json) {
        Symbol element;
        ArrayList<Symbol> parsedArray = new ArrayList<>();
        try {
            JSONArray dataArray = new JSONArray(json);

            int len = dataArray.length();
            for (int i = 0; i < len; i++) {
                element = new Symbol();
                JSONObject elementObject = dataArray.getJSONObject(i);
                element.symbol = elementObject.optString("symbol", "");
                element.name = elementObject.optString("name", "");
                element.date = elementObject.optString("date", "");
                element.isenabled = elementObject.optString("isenabled", "");
                element.type = elementObject.optString("type", "");
                element.iexid = elementObject.optString("date", "");
                parsedArray.add(element);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Could not parse to symbol " + json);
            return null;
        }
        return parsedArray;

    }

    public static ArrayList<Chart> parseChartQuote(String json) {

        /*
        "date":"20181101",
        "minute":"09:30",
        "label":"09:30 AM",
        "high":219.31,
        "low":218.79,
        "average":219.13,
        "volume":2920,
        "notional":639858.5,
        "numberOfTrades":25,
        "marketHigh":219.45,
        "marketLow":218.76,
        "marketAverage":219.069,
        "marketVolume":1048958,
        "marketNotional":229794291.211,
        "marketNumberOfTrades":3033,
        "open":219.06,
        "close":219.3,
        "marketOpen":219.07,
        "marketClose":219.33,
        "changeOverTime":0,
        "marketChangeOverTime":0},
    */
        Log.d(TAG, "parseChartData Started" + json);
        ArrayList<Chart> result = new ArrayList<>();
        Chart chart;
        try {
            JSONArray chartArray = new JSONArray(json);

            int len = chartArray.length();
            for (int i = 0; i < len; i++) {

                chart = new Chart();
                JSONObject chartObject = chartArray.getJSONObject(i);
                chart.Date = chartObject.optString("date", "");
                chart.Minute = chartObject.optString("minute", "");
                chart.High = chartObject.optDouble("high", 0);
                chart.Low = chartObject.optDouble("low", 0);
                chart.Average = chartObject.optLong("average", 0);
                chart.Volume = chartObject.optDouble("volume", 0);
                result.add(chart);
            }

        } catch (Exception ex) {
            Log.e(TAG, "parseChartQuote: " + ex.getMessage() + "    \n" + json);
        }

        Log.d(TAG, "Parsed Chart: " + result.size());
        Log.d(TAG, "parseChartData ended");
        return result;
    }


    public static Stock parseStockQuote(String json) {
        Stock element;
        Log.d(TAG, "parseStockQuote Started " + json);

        try {
            JSONObject quoteObject = new JSONObject(json);

            element = new Stock();
            // JSONObject elementObject = quoteObject.getJSONObject("quote");
            element.Symbol = quoteObject.optString("symbol", "");
            element.InPortoflio = false;
            element.Price = quoteObject.optDouble("latestPrice", 0.0);
            element.Change = quoteObject.optDouble("change", 0.0);
            element.Market = quoteObject.optString("primaryExchange", "");
            element.DayHigh = quoteObject.optDouble("high", 0.0);
            element.DayLow = quoteObject.optDouble("low", 0.0);

        } catch (Exception ex) {
            Log.e(TAG, "parseStockQuote " + ex.getMessage() + "\n" + json);
            return null;
        }
        Log.d(TAG, "paseStockQuote Ended");
        return element;

    }

    public static ArrayList<Trade> parseTradeData(JSONObject bookObject) {
        ArrayList<Trade> trades = new ArrayList<>();
        JSONArray tradeArray = null;
        try {
            tradeArray = new JSONArray(bookObject.optString("trades", "[\"\"]"));

        } catch (JSONException e) {
            Log.d(TAG, "parseTradeData: " + e.getMessage());
        }
        if (tradeArray == null) {
            return trades;
        }
        for (int i = 0; i < tradeArray.length(); i++) {

            Trade t = null;
            try {
                t = new Trade();
                JSONObject tradeObject = tradeArray.getJSONObject(i);
                double price = tradeObject.optDouble("price", 0.0);
                double size = tradeObject.optDouble("size", 0.0);
                long timestamp = tradeObject.optLong("timestamp", 0);
                t.size = size;
                t.price = price;
                t.timestamp = timestamp;
                trades.add(t);

            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }
        return trades;
    }

    public static ArrayList<Stock> parseStockDetails(String json) {

        Log.d(TAG, " entering parseReceipeJson " + json);
        Stock stock;
        ArrayList<Stock> stocks = new ArrayList<>();
        try {
            JSONArray recipeArray = new JSONArray(json);
            int len = recipeArray.length();
            for (int i = 0; i < len; i++) {
                stock = new Stock();
                JSONObject recipeObject = recipeArray.getJSONObject(i);
                stock.Symbol = recipeObject.optString("symbol", "");
                stocks.add(stock);
            }

            Log.d(TAG, " exiting parseReceipeJson - results " + stocks.get(0).toString());
            return stocks;

        } catch (Exception ex) {
            Log.e(TAG + " parseRecipeJson", "Could not parse json " + ex.toString());
            return null;
        }
    }

    public static String parseLogo(String json) {
        String url = "";
        Log.d(TAG, "parseLogo Started " + json);

        try {
            JSONObject logoObject = new JSONObject(json);

            url = logoObject.optString("url", "");

        } catch (Exception ex) {
            Log.e(TAG, "parseStockQuote " + ex.getMessage() + "\n" + json);
            return null;
        }
        Log.d(TAG, "paseStockQuote Ended");
        return url;
    }
}

