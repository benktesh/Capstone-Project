package benktesh.smartstock.Utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

import benktesh.smartstock.Data.SmartStockContract;
import benktesh.smartstock.Data.SmartStrockDbHelper;
import benktesh.smartstock.Model.SearchRow;
import benktesh.smartstock.Model.Stock;
import benktesh.smartstock.Model.Symbol;
import benktesh.smartstock.R;

/**
 * Created by Benktesh on 5/1/18.
 * Some of the resource for this file were created from Udacity provided content for the students in Google Challenge Scholar's Exercise 2.
 */

//TODO - this is from old code. Needs cleanup perhaps rename

public class NetworkUtilities {
    private static final String TAG = NetworkUtilities.class.getSimpleName();
    private static SmartStrockDbHelper mDbHelper;
    private static SQLiteDatabase db;

    private static ArrayList<Stock> searchResult = new ArrayList<>();


    // Api related parameters
    public static final String BaseAddress = "https://api.iextrading.com/1.0";
    public static final String SymbolURL = "https://api.iextrading.com/1.0/ref-data/symbols";
    public static final String STOCKURL = "https://api.iextrading.com/1.0/stock/";


    public static boolean populateSymbol(Context context, boolean force) {
        String result = "";
        if (force == true) {
            result = getSymbols(context);
        } else {
            //check database for updateflag for symbolentry table
            //if no record found populate symbol
            result = getSymbols(context);
        }

        ArrayList<Symbol> dataArray = JsonUtilities.parseSymbol(result);
        if (dataArray == null) {
            Log.e(TAG, "No data. Could not parse json");
            return false;
        }

        mDbHelper = new SmartStrockDbHelper(context);
        // Gets the data repository in write mode
        db = mDbHelper.getWritableDatabase();
        ContentValues values;
        long rows = db.delete(SmartStockContract.SymbolEntry.TABLE_NAME,null, null);
        Log.d(TAG, "Deleted # of rows: " + rows);
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
            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(SmartStockContract.SymbolEntry.TABLE_NAME, null, values);
            //Log.d(TAG, "Loading Symbol: " + symbol.symbol + " (" + i + ")");
        }

        values = new ContentValues();
        values.put(SmartStockContract.UpdateEntry.COLUMN_TABLE, SmartStockContract.SymbolEntry.TABLE_NAME);
        values.put(SmartStockContract.UpdateEntry.COLUMN_DATE, new Date().toString());

        long newRowId = db.insert(SmartStockContract.UpdateEntry.TABLE_NAME, null, values);
        db.close();

        Log.d(TAG, "Done updating Symbol: ");

        Log.d(TAG, result);
        return result != null;
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

    public static ArrayList<Stock> getMarketData() {
        //load the market stat from Database
        //get latest data from api
        //parse through json and return the object
        Log.d(TAG, "getMarketData: ");
        ArrayList<Stock> marketData = new ArrayList<>();

        marketData.add(makeMarket("SPY", 1000.0, 1.0, "NYSE"));
        marketData.add(makeMarket("DJI", 1000.0, 1.0, "SYSE"));

        return marketData;
    }

    private static Stock makeMarket(String symbol, Double price, Double change, String marketInfo) {

        Stock m = new Stock();
        m.Symbol = symbol;
        m.Price = price;
        m.Change = change;
        m.Market = marketInfo;
        m.IsMarket = true;
        return m;
    }

    public static boolean addPortfolio(Context context, String symbol)
    {
        Log.d(TAG, "Starting addPortfolio" );
        mDbHelper = new SmartStrockDbHelper(context);
        // Gets the data repository in write mode
        db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SmartStockContract.PortfolioEntry.COLUMN_SYMBOL, symbol);
        long newRowId = db.insert(SmartStockContract.PortfolioEntry.TABLE_NAME, null, values);
        db.close();
        Log.d(TAG, "Completing addPortfolio" );
        return true;
    }

    public static boolean removePortfolio(Context context, String symbol)
    {
        Log.d(TAG, "Starting removePortfolio" );
        mDbHelper = new SmartStrockDbHelper(context);
        // Gets the data repository in write mode
        db = mDbHelper.getWritableDatabase();
        boolean result =  db.delete(SmartStockContract.PortfolioEntry.TABLE_NAME,
                SmartStockContract.PortfolioEntry.COLUMN_SYMBOL + " = '" + symbol + "'", null) > 0;
        db.close();
        Log.d(TAG, "Completing removePortfolio" );
        return result;
    }

    public static ArrayList<Stock> searchStock(Context context, String query) {
        Log.d(TAG, "Start SearchStock");
        ArrayList<Stock> result = new ArrayList<>();
        if (query == null || query.length() == 0) {
            Log.d(TAG, "Empty query string: " + query);
            return result;
        }

        mDbHelper = new SmartStrockDbHelper(context);
        // Gets the data repository in write mode
        db = mDbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT " +
                SmartStockContract.SymbolEntry.COLUMN_SYMBOL +
                " FROM " + SmartStockContract.SymbolEntry.TABLE_NAME + " Where " +
                SmartStockContract.SymbolEntry.COLUMN_SYMBOL + " = '" + query +"' "
                , null);
        ArrayList<String> matchingSymbol = new ArrayList<>();
        if (c.moveToFirst()){
            do {
                String symbol = c.getString(0);
                matchingSymbol.add(symbol);
            } while(c.moveToNext());
        }
        c.close();

        ArrayList<String> portfolio = getPortfolio();

        db.close();
        Log.d(TAG, "Matching Symbols Count: " + matchingSymbol.size());

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
        for(int i = 0; i < matchingSymbol.size(); i++){
            String symbol = matchingSymbol.get(i);
            Log.d(TAG, "Getting Detailed Data for " + symbol);
            try {
                String stockUrl = STOCKURL + symbol + "/quote";
                URL url = new URL(stockUrl);
                String response = getResponseFromHttpUrl(url, context);
                Log.d(TAG, "Details for " + symbol + " : " + response );
                Stock parsedData = JsonUtilities.parseStockQuote(response);
                //if this stock is in portfolio, then mark it true
                if(portfolio != null && portfolio.contains(symbol)) {
                    parsedData.InPortoflio = true;
                }
                searchResult.add(parsedData);
            }
            catch (Exception ex)
            {
                Log.e(TAG, ex.toString());
                return null;
            }
        }
        return searchResult;
    }

    @NonNull
    private static ArrayList<String> getPortfolio() {
        Cursor c;
        ArrayList<String> portfolio = new ArrayList<>();
        c = db.rawQuery("SELECT symbol FROM portfolio", null);
        if (c.moveToFirst()) {
            do {
                portfolio.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        return portfolio;
    }

    /*
    This method returns a list of stock data based on query.
    If query is for Portfolio, it will return list of stocks in portfolio
    Else it searches for query string, and returns a list of matching stock items (some paritial matach and upto 10 results)
     */
    public static ArrayList<Stock> getStockData(Context context, String query) {
        Log.d(TAG, "Starting getStockData: " + query);
        if (query.equals(SmartStockConstant.PortfolioQueryString)) {

            mDbHelper = new SmartStrockDbHelper(context);
            // Gets the data repository in write mode
            db = mDbHelper.getWritableDatabase();

            ArrayList<String> portfolioSymbol = getPortfolio();

            ArrayList<Stock> portfolio = getDetails(context, portfolioSymbol, portfolioSymbol);

            db.close();
            Log.d(TAG, "Completing Portfolio" );

            //TODO save to network
            return portfolio;

            /*
            searchResult.add(new Stock("EGOV", 1.0,
                    false, "NASDAQ", 100.0, 99.0, 100.0, false));
            searchResult.add(new Stock("SPY", 1.0,
                    true, "NYSE", 100.0, 99.0, 100.0, false));

            searchResult.add(new Stock("ARR", 1.0,
                    false, "NYSE", 100.0, 99.0, 100.0, false));

            searchResult.add(new Stock("GE", 1.0,
                    true, "NYSE", 100.0, 99.0, 100.0, false));

            searchResult.add(new Stock("SPY", 1.0,
                    true, "NYSE", 100.0, 99.0, 100.0, false));
                    */
        } else {
            //search for query string on api
            //return a list upto matching number
            searchResult.add(new Stock("EGOV", 1.0,
                    false, "NASDAQ", 100.0, 99.0, 100.0, false));
            searchResult.add(new Stock("SPY", 1.0,
                    true, "NYSE", 100.0, 99.0, 100.0, false));

            searchResult.add(new Stock("ARR", 1.0,
                    false, "NYSE", 100.0, 99.0, 100.0, false));

            searchResult.add(new Stock("GE", 1.0,
                    true, "NYSE", 100.0, 99.0, 100.0, false));

            searchResult.add(new Stock("SPY", 1.0,
                    true, "NYSE", 100.0, 99.0, 100.0, false));

            Log.d(TAG, "Original searchresuult size: " + searchResult.size()
                    + " max searchsize: " + SmartStockConstant.MaximumSearchResult);

            LibraryHelper.Trim(searchResult, SmartStockConstant.MaximumSearchResult);
        }


        return searchResult;
    }
}
