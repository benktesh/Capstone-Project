package benktesh.smartstock.Utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import benktesh.smartstock.Model.SearchRow;
import benktesh.smartstock.Model.Stock;
import benktesh.smartstock.R;

/**
 * Created by Benktesh on 5/1/18.
 * Some of the resource for this file were created from Udacity provided content for the students in Google Challenge Scholar's Exercise 2.
 */

//TODO - this is from old code. Needs cleanup perhaps rename

public class NetworkUtilities {
    private static final String TAG = NetworkUtilities.class.getSimpleName();


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

    public static String getBitmapString(String fileUrl) {
        String bitmapString = null;
        try {
            URL myFileUrl = new URL(fileUrl);
            HttpURLConnection conn =
                    (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            Bitmap temp = BitmapFactory.decodeStream(is);
            bitmapString = encodeToBase64(temp);

        } catch (IOException e) {
            Log.e(TAG, "getMap:" + e.getStackTrace());
        }
        return bitmapString;
    }


    public static String encodeToBase64(Bitmap image) {
        if (image == null)
            return null;
        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        if (input == null || input.isEmpty())
            return null;
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    public static Bitmap GetBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (NullPointerException e) {
            e.getMessage();
            return null;
        } catch (OutOfMemoryError e) {
            return null;
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
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    private static String getResponseFromHttpUrl(URL url, Context context) throws IOException {
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

    /*
    This method returns a list of stock data based on query.
    If query is for Portfolio, it will return list of stocks in portfolio
    Else it searches for query string, and returns a list of matching stock items (some paritial matach and upto 10 results)
     */
    public static ArrayList<Stock> getStockData(String query) {
        Log.d(TAG, "getStockData: " + query);
        ArrayList<Stock> searchResult = new ArrayList<>();
        if(query.equals(SmartStockConstant.PortfolioQueryString)) {

            //load portfolio data from database
            //get latest stat from network
            //save latest into db
            //return data
            searchResult.add(new Stock ("EGOV", 1.0,
                    false, "NASDAQ", 100.0, 99.0, 100.0, false));
            searchResult.add(new Stock ("SPY", 1.0,
                    true, "NYSE", 100.0, 99.0, 100.0, false));

            searchResult.add(new Stock ("ARR", 1.0,
                    false, "NYSE", 100.0, 99.0, 100.0, false));

            searchResult.add(new Stock ("GE", 1.0,
                    true, "NYSE", 100.0, 99.0, 100.0, false));

            searchResult.add(new Stock ("SPY", 1.0,
                    true, "NYSE", 100.0, 99.0, 100.0, false));
        }
        else {
            //search for query string on api
            //return a list upto matching number

            LibraryHelper.Trim(searchResult, SmartStockConstant.MaximumSearchResult);
        }



        return searchResult;
    }
}
