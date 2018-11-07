package benktesh.smartstock.Utils;


import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

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
        Stock element = null;
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

    public static ArrayList<Trade>  parseTradeData(JSONObject bookObject) {
        ArrayList<Trade> trades = new ArrayList<>();


        JSONArray tradeArray = null;
        try {
            tradeArray = new JSONArray(bookObject.optString("trades", "[\"\"]"));
        } catch (JSONException e) {
            Log.d(TAG, "parseTradeData: " + e.getMessage());
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

                /*
                JSONArray ingredientsArray = new JSONArray(recipeObject.optString("ingredients",
                        "[\"\"]"));
                List<Ingredient> ingredients = new ArrayList<>();
                Ingredient ingredient;
                for (int j = 0; j < ingredientsArray.length(); j++) {
                    ingredient = new Ingredient();

                    JSONObject ingredientObject = ingredientsArray.getJSONObject(j);
                    ingredient.quantity = ingredientObject.optDouble("quantity", 0);
                    ingredient.ingredient = ingredientObject.optString("ingredient", "");
                    ingredient.measure = ingredientObject.optString("measure", "");
                    ingredients.add(ingredient);
                }
                stock.ingredients = ingredients;


                JSONArray stepsArray = new JSONArray(recipeObject.optString("steps",
                        "[\"\"]"));

                List<Step> steps = new ArrayList<>();
                Step step;
                for (int j = 0; j < stepsArray.length(); j++) {
                    step = new Step();

                    JSONObject object = stepsArray.getJSONObject(j);
                    step.id = object.optInt("id", 0);
                    step.shortDescription = object.optString("shortDescription", "");
                    step.description = object.optString("description", "");
                    step.videoURL = object.optString("videoURL", "");
                    step.thumbnailURL = object.optString("thumbnailURL", "");
                    steps.add(step);
                }
                stock.steps = steps;

                recipies.add(stock);
                */

                stocks.add(stock);

            }

            Log.d(TAG, " exiting parseReceipeJson - results " + stocks.get(0).toString());

            return stocks;

        } catch (Exception ex) {
            Log.e(TAG + " parseRecipeJson", "Could not parse json " + ex.toString());
            return null;
        }
    }

    /*
    public static List<MovieReview> parseMovieReviewJson(String json) {
        try {
            MovieReview review;
            JSONObject object = new JSONObject(json);

            JSONArray resultsArray = new JSONArray(object.optString("results",
                    "[\"\"]"));

            //    {"id":19404,"page":1,"results":[{"author":"MohamedElsharkawy","content":"The Dilwale Dulhania Le Jayenge is a film considered by most to be one of the greatest ever made. From The American Film Institute to as voted by users on the Internet Movie Database (IMDB) it is consider to be one of the best.","id":"59eb3d42925141565100e901","url":"https://www.themoviedb.org/review/59eb3d42925141565100e901"}],"total_pages":1,"total_results":1}
            System.out.println(resultsArray.toString());

            ArrayList<MovieReview> items = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                String current = resultsArray.optString(i, "");
                JSONObject reviewJson = new JSONObject(current);

                String content = reviewJson.optString("content", "Not Available");
                String author = reviewJson.optString("author", "Not Available");

                review = new MovieReview(author, content);
                items.add(review);
            }
            return items;

        } catch (Exception ex) {
            Log.e(TAG + "parseMovieJson", "Could not parse json " + json);
            return null;
        }

    }

    public static List<MovieVideo> parseMovieVideoJson(String json) {
        try {
            MovieVideo movieVideo;
            JSONObject object = new JSONObject(json);

            JSONArray resultsArray = new JSONArray(object.optString("results",
                    "[\"\"]"));

            //{"id":19404,
            // "results":[
            // {"id":"5581bd68c3a3685df70000c6","iso_639_1":"en","iso_3166_1":"US","key":"c25GKl5VNeY","name":"Dilwale Dulhania Le Jayenge | Official Trailer | Shah Rukh Khan | Kajol","site":"YouTube","size":720,"type":"Trailer"},{"id":"58e9bfb6925141351f02fde0","iso_639_1":"en","iso_3166_1":"US","key":"Y9JvS2TmSvA","name":"Mere Khwabon Mein - Full Song | Dilwale Dulhania Le Jayenge | Shah Rukh Khan | Kajol","site":"YouTube","size":720,"type":"Clip"},{"id":"58e9bf11c3a36872ee070b9a","iso_639_1":"en","iso_3166_1":"US","key":"H74COj0UQ_Q","name":"Zara Sa Jhoom Loon Main - Dilwale Dulhania Le Jayenge (1995) 720p HD","site":"YouTube","size":720,"type":"Clip"},{"id":"58e9c00792514152ac020a34","iso_639_1":"en","iso_3166_1":"US","key":"OkjXMqK1G0o","name":"Ho Gaya Hai Tujhko Toh Pyar Sajna - Full Song - Dilwale Dulhania Le Jayenge","site":"YouTube","size":720,"type":"Clip"},{"id":"58e9c034c3a36872ee070c84","iso_639_1":"en","iso_3166_1":"US","key":"7NhoeyoR_XA","name":"Mehandi Laga Ke Rakhna - Dilwale Dulhaniya Le Jayenge (Full HD 1080p)","site":"YouTube","size":720,"type":"Clip"},{"id":"58e9c07f9251414b2802a16e","iso_639_1":"en","iso_3166_1":"US","key":"Ee-cCwP7VPQ","name":"Tujhe Dekha To (Dilwale Dulhania Le Jaayenge) Piano Cover feat. Aakash Gandhi","site":"YouTube","size":480,"type":"Clip"}]}
            System.out.println(resultsArray.toString());

            ArrayList<MovieVideo> items = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                String current = resultsArray.optString(i, "");
                JSONObject reviewJson = new JSONObject(current);

                String id = reviewJson.optString("id", "Not Available");
                String key = reviewJson.optString("key", "Not Available");

                movieVideo = new MovieVideo(id, key);
                items.add(movieVideo);
            }
            return items;

        } catch (Exception ex) {
            Log.e(TAG + "parseMovieJson", "Could not parse json " + json);
            return null;
        }

    }
    */
}

