package benktesh.smartstock.Utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

import benktesh.smartstock.Data.SmartStockContract;
import benktesh.smartstock.Data.SmartStrockDbHelper;
import benktesh.smartstock.Model.Chart;
import benktesh.smartstock.Model.Stock;
import benktesh.smartstock.Model.Symbol;
import benktesh.smartstock.R;

import static benktesh.smartstock.Data.SmartStockContract.MarketEntry.COLUMN_SYMBOL;

/**
 * Created by Benktesh on 5/1/18.
 * Some of the resource for this file were created from Udacity provided content for the students in Google Challenge Scholar's Exercise 2.
 */

//TODO - this is from old code. Needs cleanup perhaps rename

public class NetworkUtilities {
    // Api related parameters
    public static final String BaseAddress = "https://api.iextrading.com/1.0";
    public static final String SymbolURL = "https://api.iextrading.com/1.0/ref-data/symbols";
    public static final String STOCKURL = "https://api.iextrading.com/1.0/stock/";
    public static final String CHARTSUFFIX = "/chart/1d";
    public static final String BOOK_SUFFIX = "/book";
    public static final String QUOTE_SUFFIX = "/quote";
    public static final String LOGO_SUFFIX = "/logo";
    private static final String TAG = NetworkUtilities.class.getSimpleName();
    private static SmartStrockDbHelper mDbHelper;
    private static SQLiteDatabase db;
    private static ArrayList<Stock> searchResult = new ArrayList<>();

    public static boolean populateSymbol(Context context, boolean force) {
        Log.d(TAG, "Populating Symbols");

        //startout marketSymbol
        loadMarketSymbols(context, SmartStockConstant.MarketSymbols, force);

        String result = "";
        if (force == true) {
            Log.d(TAG, "Loading new sets of symbols.");
            result = getSymbols(context);
        } else {
            String[] columns = new String[]{SmartStockContract.AuditEntry.COLUMN_TABLE};
            Cursor c = context.getContentResolver().query(SmartStockContract.AuditEntry.AUDIT_URI, columns,
                    null, null, null, null);
            if (c != null && c.getCount() > 0) {
                //Log.d(TAG,"Database has previously been populated. Returning true");
                c.close();
                return true;
            }
            Log.d(TAG, "Loading new sets of symbols.");
            result = getSymbols(context);
        }

        Log.i(TAG, "Symbols loading: " + result);

        ArrayList<Symbol> dataArray = JsonUtilities.parseSymbol(result);
        if (dataArray == null) {
            Log.e(TAG, "No data. Could not parse json");
            return false;
        }


        ContentValues values;

        long rows;
        rows = context.getContentResolver().delete(SmartStockContract.SymbolEntry.SYMBOL_URI,null, null);
        Log.d(TAG, "Deleted # of rows in Table " + SmartStockContract.SymbolEntry.TABLE_NAME + rows);


        String[] selectionArgs = {SmartStockContract.SymbolEntry.TABLE_NAME};
        rows = context.getContentResolver().delete(SmartStockContract.AuditEntry.AUDIT_URI,
                SmartStockContract.AuditEntry.COLUMN_TABLE + " =?", selectionArgs);
        Log.d(TAG, "Deleted # of rows in Table " + SmartStockContract.AuditEntry.TABLE_NAME + rows);

        for (int i = 0; i < dataArray.size(); i++) {
            Symbol symbol = dataArray.get(i);
            // Create a new map of values, where column names are the keys
            values = new ContentValues();
            values.put(SmartStockContract.SymbolEntry.COLUMN_SYMBOL, symbol.symbol);
            values.put(SmartStockContract.SymbolEntry.COLUMN_NAME, symbol.name);
            values.put(SmartStockContract.SymbolEntry.COLUMN_DATE, symbol.date);
            values.put(SmartStockContract.SymbolEntry.COLUMN_IEXID, symbol.iexid);
            values.put(SmartStockContract.SymbolEntry.COLUMN_ISENABLED, symbol.isenabled);
            values.put(SmartStockContract.SymbolEntry.COLUMN_TYPE, symbol.type);
            context.getContentResolver().insert(SmartStockContract.SymbolEntry.SYMBOL_URI, values);
        }

        values = new ContentValues();
        values.put(SmartStockContract.AuditEntry.COLUMN_TABLE, SmartStockContract.SymbolEntry.TABLE_NAME);
        values.put(SmartStockContract.AuditEntry.COLUMN_DATE, new Date().toString());
        context.getContentResolver().insert(SmartStockContract.AuditEntry.AUDIT_URI, values);
        Log.d(TAG, "Done updating Symbol: " + result);
        return result != null;
    }

    public static void loadMarketSymbols(Context context, String[] marketSymbols, boolean force) {
        try {
            Log.d(TAG, "loadMarketSymbols");
            Cursor c;
            String[] columns = new String[]{SmartStockContract.AuditEntry.COLUMN_TABLE};
            String selection = SmartStockContract.AuditEntry.COLUMN_TABLE + " =?";
            String[] selectionArguments = {SmartStockContract.MarketEntry.TABLE_NAME };

            c = context.getContentResolver().query(
                    SmartStockContract.AuditEntry.AUDIT_URI,
                    columns, selection, selectionArguments, null);

            if (force == false && c != null && c.getCount() > 0) {
                Log.d(TAG, "Market symbols have previously been populated.");

            } else {
                long rows;
                //long rows = db.delete(SmartStockContract.MarketEntry.TABLE_NAME, null, null);
                rows = context.getContentResolver().delete(SmartStockContract.MarketEntry.MARKET_URI, null, null);
                Log.d(TAG, "Deleted # of rows in Table " + SmartStockContract.MarketEntry.TABLE_NAME + rows);

                rows = context.getContentResolver().delete(SmartStockContract.AuditEntry.AUDIT_URI, null, null);
                Log.d(TAG, "Deleted # of rows in Table " + SmartStockContract.AuditEntry.TABLE_NAME + rows);

                ContentValues values;
                for (String symbol : marketSymbols) {
                    values = new ContentValues();
                    values.put(COLUMN_SYMBOL, symbol);
                   // db.insert(SmartStockContract.MarketEntry.TABLE_NAME, null, values);
                    context.getContentResolver().insert(SmartStockContract.MarketEntry.MARKET_URI,values);
                }

                values = new ContentValues();
                values.put(SmartStockContract.AuditEntry.COLUMN_TABLE, SmartStockContract.MarketEntry.TABLE_NAME);
                values.put(SmartStockContract.AuditEntry.COLUMN_DATE, new Date().toString());
                context.getContentResolver().insert(SmartStockContract.AuditEntry.AUDIT_URI,values);
            }
            c.close();
        } catch (Exception ex) {
            Log.e(TAG, "loadMarketSymbols: " + ex.toString());
        }
    }

    /*
    Method gets list of symbols supported by API
     */
    private static String getSymbols(Context context) {
        URL url = null;
        String response;
        try {
            url = new URL(SymbolURL);

            response = getResponseFromHttpUrl(url, context);
            return response;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /*
    This method returns the list of Recipe from json
     */
    public static ArrayList<Stock> stockDetails(Context context) {
        Log.d(TAG, "Calling context: " + context);

        String jsonText = null;

        if (!isOnline(context) && false) {
            Log.e(TAG, "There is no network connection. Using local file");
            jsonText = getLocal(context);

        } else {
            //read from network
            Log.e(TAG, "There is network connection. Getting data from network");

            Uri builtUri = Uri.parse(context.getString(R.string.api_URL));
            URL url = getUrl(builtUri);
            try {
                jsonText = getResponseFromHttpUrl(getUrl(builtUri), context);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, Arrays.toString(e.getStackTrace()));
            } finally {
                if (jsonText == null) {
                    Log.e(TAG, "Failed get recipe from network. Using local file");
                    jsonText = getLocal(context);
                }
            }
        }
        if (jsonText == null) {

            return null;
        }
        Log.d(TAG, jsonText);
        return JsonUtilities.parseStockDetails(jsonText);

    }


    public static String getResponseFromHttpUrl(URL url, Context context) throws IOException {
        if (!isOnline(context)) {
            Log.e(TAG, "There is no network connection");
            return null;
        }
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(10000); //sets connection timeout to 10 seconds
        urlConnection.setReadTimeout(20000); //sets read time out to 20 seconds
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                scanner.close();
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


    private static String getLocal(Context context) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        return sb.toString();

    }

    @Nullable
    private static URL getUrl(Uri builtUri) {
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    /**
     * This method checks network connection. This code was derived from
     * https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     **/
    private static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm != null ? cm.getActiveNetworkInfo() : null;
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean addPortfolio(Context context, String symbol) {
        Log.d(TAG, "Starting addPortfolio");
        ContentValues values = new ContentValues();
        values.put(SmartStockContract.PortfolioEntry.COLUMN_SYMBOL, symbol);
        Uri uri = context.getContentResolver().insert(SmartStockContract.PortfolioEntry.PORTFOLIO_URI, values);
        Log.d(TAG, "Completing addPortfolio: " + uri);
        return true;
    }

    public static boolean removePortfolio(Context context, String symbol) {
        Log.d(TAG, "Starting removePortfolio");
        // Defines selection criteria for the rows you want to delete
        String mSelectionClause = SmartStockContract.PortfolioEntry.COLUMN_SYMBOL + " = ?";
        String[] mSelectionArgs = {symbol};
        int x = context.getContentResolver().delete(SmartStockContract.PortfolioEntry.PORTFOLIO_URI,
                mSelectionClause, mSelectionArgs);
        Log.d(TAG, "Completing removePortfolio " + x);
        return true;
    }

    public static ArrayList<Stock> searchStock(Context context, String query) {
        ArrayList<Stock> result = new ArrayList<>();

        //check for null;
        if (query == null || query.length() == 0) {
            Log.d(TAG, "Empty query string: " + query);
            return null;
        }
        Log.d(TAG, "Start SearchStock: " + query);

        String[] cols = {SmartStockContract.SymbolEntry.COLUMN_SYMBOL};
        String[] selectionArguements = {query};
        Cursor c = context.getContentResolver().query(SmartStockContract.SymbolEntry.SYMBOL_URI, cols,
                SmartStockContract.SymbolEntry.COLUMN_SYMBOL + " =?", selectionArguements, null );


        ArrayList<String> matchingSymbol = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                String symbol = c.getString(0);
                matchingSymbol.add(symbol);
            } while (c.moveToNext());
        }

        Log.d(TAG, "Matching Symbols Count: " + matchingSymbol.size());
        ArrayList<String> portfolio = getListPortfolio(context);
        Log.d(TAG, " Portfolio Size: " + portfolio.size());
        //Instead of loop, use batch

        searchResult = getDetails(context, matchingSymbol, portfolio);
        Log.d(TAG, "End SearchStock " + searchResult.size() + " " + searchResult.toString());
        //LibraryHelper.Trim(searchResult, SmartStockConstant.MaximumSearchResult);

        return searchResult;

    }

    /*
    This method returns details of stock
     */
    private static ArrayList<Stock> getDetails(Context context,
                                               ArrayList<String> matchingSymbol,
                                               ArrayList<String> portfolio) {

        ArrayList<Stock> searchResult = new ArrayList<>();
        for (int i = 0; i < matchingSymbol.size(); i++) {
            String symbol = matchingSymbol.get(i);
            Log.d(TAG, "Getting Detailed Data for " + symbol);
            try {
                String stockUrl = STOCKURL + symbol + QUOTE_SUFFIX;
                Log.d(TAG, "getDetails: " + symbol);
                URL url = new URL(stockUrl);
                String response = getResponseFromHttpUrl(url, context);
                Log.d(TAG, "Details for quote for " + symbol + " : " + response);
                Stock parsedData = JsonUtilities.parseStockQuote(response);
                //if this stock is in portfolio, then mark it true
                if (portfolio != null && portfolio.contains(symbol)) {
                    parsedData.InPortoflio = true;
                }

                url = new URL(STOCKURL + symbol + CHARTSUFFIX);
                Log.d(TAG, "getDetails calling chart: " + url);
                response = getResponseFromHttpUrl(url, context);
                Log.d(TAG, "Detail for chart Data for " + symbol + " : " + response);

                ArrayList<Chart> chartData = JsonUtilities.parseChartQuote(response);
                parsedData.Charts = chartData;

                //Get Logo URL
                url = new URL(STOCKURL + symbol + LOGO_SUFFIX);
                Log.d(TAG, "getDetails calling logo: " + url);
                response = getResponseFromHttpUrl(url, context);
                parsedData.LogoUrl = JsonUtilities.parseLogo(response);

                searchResult.add(parsedData);
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
                return null;
            }
        }
        return searchResult;
    }

    /*
        This method returns a list of stocks as market data using Content Provider
     */
    public static ArrayList<String> getListMarket(Context context) {
        Log.d(TAG, " Starting getListMarket");

        ArrayList<String> data = new ArrayList<>();

        String[] cols = {SmartStockContract.MarketEntry.COLUMN_SYMBOL};
        Cursor c = context.getContentResolver().query(SmartStockContract.MarketEntry.MARKET_URI,
                cols, null, null, null);

        if (c.moveToFirst()) {
            do {
                data.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        Log.d(TAG, " Ending getListMarket");
        return data;
    }


    @NonNull
    private static ArrayList<String> getListPortfolio(Context context) {
        Log.d(TAG, " Getting Portfolio");
        ArrayList<String> portfolio = new ArrayList<>();

        String[] cols = {SmartStockContract.MarketEntry.COLUMN_SYMBOL};
        Cursor c = context.getContentResolver().query(SmartStockContract.PortfolioEntry.PORTFOLIO_URI
                , cols, null, null, null);

        if (c.moveToFirst()) {
            do {
                portfolio.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        Log.d(TAG, " Ending Portfolio");
        return portfolio;
    }


    /*
    This method returns a list of stock data based on query.
    If query is for Portfolio, it will return list of stocks in portfolio
    If query is for Market, it will return list of stocks in market
    Else it searches for query string, and returns a list of matching stock items (some paritial matach and upto 10 results)
     */
    public static ArrayList<Stock> getStockData(Context context, String query) {
        Log.d(TAG, "Starting getStockData: " + query);

        if (query.equals(SmartStockConstant.QueryPortfolio)) {
            ArrayList<String> portfolioSymbol = getListPortfolio(context);
            ArrayList<Stock> portfolio = getDetails(context, portfolioSymbol, portfolioSymbol);
            Log.d(TAG, "Completing Portfolio " + portfolio.size());
            return portfolio;
        } else if (query.equals(SmartStockConstant.QueryMarket)) {
            ArrayList<String> symbols = getListMarket(context);
            ArrayList<String> portfolioSymbol = getListPortfolio(context);
            ArrayList<Stock> markets = getDetails(context, symbols, portfolioSymbol);
            Log.d(TAG, "Completing Portfolio " + markets.size());
            return markets;
        }

        return searchResult;
    }
}
