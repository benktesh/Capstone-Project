package benktesh.smartstock;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import benktesh.smartstock.Model.Stock;
import benktesh.smartstock.UI.CommonUIHelper;
import benktesh.smartstock.UI.StockDetailActivity;
import benktesh.smartstock.Utils.NetworkUtilities;
import benktesh.smartstock.Utils.SmartStockConstant;

import static benktesh.smartstock.Utils.SmartStockConstant.ParcelableStock;

public class SearchActivity extends AppCompatActivity implements SearchAdapter.ListItemClickListener {

    private static String TAG = SearchActivity.class.getSimpleName();

    CommonUIHelper mCommonUIHelper;
    ArrayList<Stock> mData;

    /*
     * References to RecyclerView and Adapter to reset the list to its
     * "pretty" state when the reset menu item is clicked.
     */
    private SearchAdapter mAdapter;
    private RecyclerView mSearchList;
    private Toast mToast;

    private ProgressBar spinner;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (mCommonUIHelper == null) {
            mCommonUIHelper = new CommonUIHelper(this);
        }

        spinner = findViewById(R.id.progressbar);

        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mSearchList = findViewById(R.id.rv_stocks);

        /*
         * A LinearLayoutManager is responsible for measuring and positioning item views within a
         * RecyclerView into a linear list. This means that it can produce either a horizontal or
         * vertical list depending on which parameter you pass in to the LinearLayoutManager
         * constructor. By default, if you don't specify an orientation, you get a vertical list.
         * In our case, we want a vertical list, so we don't need to pass in an orientation flag to
         * the LinearLayoutManager constructor.
         *
         * There are other LayoutManagers available to display your data in uniform grids,
         * staggered grids, and more! See the developer documentation for more details.
         */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mSearchList.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mSearchList.setHasFixedSize(true);
        mAdapter = new SearchAdapter(mData, this);
        mSearchList.setAdapter(mAdapter);

        Intent intent = getIntent();
        //if the intent is coming from widget, search for stock and display
        Stock stock = intent.getParcelableExtra(SmartStockConstant.ParcelableStock);
        if(stock != null) {
            new NetworkQueryTask().execute(stock.Symbol.toUpperCase());
        }
        else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            new NetworkQueryTask().execute(query.toUpperCase());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mCommonUIHelper.MakeMenu(item)) return true;
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void ShowMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListItemClick(Stock stock) {
        if (mToast != null) {
            mToast.cancel();
        }

        Intent intent = new Intent(this.getApplicationContext(), StockDetailActivity.class);
        intent.putExtra(ParcelableStock, stock);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, bundle);
    }


    //asynctask handling
    class NetworkQueryTask extends AsyncTask<String, Void, ArrayList<Stock>> {

        private String query;

        @Override
        protected ArrayList<Stock> doInBackground(String... params) {
            query = params[0];


            ArrayList<Stock> searchResults = null;
            try {

                searchResults = NetworkUtilities.searchStock(getApplicationContext(), query);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(ArrayList<Stock> searchResults) {
            super.onPostExecute(searchResults);

            if (query != null) {
                if (searchResults != null) {
                    if (searchResults.size() == 0) {
                        Toast.makeText(getApplicationContext(), getString(R.string.stock_not_found), Toast.LENGTH_LONG).show();
                    }
                    mAdapter.resetData(searchResults);
                    spinner.setVisibility(View.INVISIBLE);
                }
            } else {
                Log.e(TAG, "onPostExecute: Query is Null in Async. Nothing Done");
            }
        }
    }
}

