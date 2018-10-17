package benktesh.smartstock.UI;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import benktesh.smartstock.MainActivity;
import benktesh.smartstock.Model.Stock;
import benktesh.smartstock.R;
import benktesh.smartstock.SearchAdapter;
import benktesh.smartstock.Model.SearchRow;
import benktesh.smartstock.Utils.NetworkUtilities;
import benktesh.smartstock.Utils.SmartStockConstant;

public class PortfolioActivity extends AppCompatActivity implements SearchAdapter.ListItemClickListener {

    private static String TAG = PortfolioActivity.class.getSimpleName();

    CommonUIHelper mCommonUIHelper;
    ArrayList<Stock> mData;


    /*
     * References to RecyclerView and Adapter to reset the list to its
     * "pretty" state when the reset menu item is clicked.
     */
    private SearchAdapter mAdapter;
    private RecyclerView mSearchList;
    private Toast mToast;

    private ProgressBar mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (mCommonUIHelper == null) {
            mCommonUIHelper = new CommonUIHelper(this);
        }

        mSearchList = findViewById(R.id.rv_stocks);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mSearchList.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mSearchList.setHasFixedSize(true);


        mAdapter = new SearchAdapter(mData, this);
        mSearchList.setAdapter(mAdapter);

        mSpinner = (ProgressBar) findViewById(R.id.progressbar);

        mAdapter.resetData(mData);

        new NetworkQueryTask().execute(SmartStockConstant.PortfolioQueryString);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return mCommonUIHelper.ConfigureSearchFromMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mCommonUIHelper.MakeMenu(item)) return true;
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onListItemClick(Stock stock) {
        mCommonUIHelper.showToast(stock.Symbol);
        Intent startChildActivityIntent = new Intent(this.getApplicationContext(), StockDetailActivity.class);
        startActivity(startChildActivityIntent);
    }

    class NetworkQueryTask extends AsyncTask<String, Void, ArrayList<Stock>> {

        private String query;

        @Override
        protected ArrayList<Stock> doInBackground(String... params) {
            query = params[0];
            if (mSpinner != null) {
                //mSpinner.setVisibility(View.VISIBLE);
            }
            ArrayList<Stock> searchResults = null;
            try {

                searchResults = NetworkUtilities.getStockData(SmartStockConstant.PortfolioQueryString);

                Log.d(TAG, query + ": Calling getMarketData() " + " " + searchResults.size());


            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(ArrayList<Stock> searchResults) {
            super.onPostExecute(searchResults);

            if (query != null) {
                if (searchResults != null && searchResults.size() != 0) {

                    mAdapter.resetData(searchResults);


                } else {
                    Toast.makeText(getApplicationContext(), R.string.Network_Error_Prompt, Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e(TAG, "onPostExecute: Query is Null in Async. Nothing Done");
            }

            if (mSpinner.getVisibility() == View.VISIBLE) {
                //mSpinner.setVisibility(View.GONE);
            }
        }
    }
}
