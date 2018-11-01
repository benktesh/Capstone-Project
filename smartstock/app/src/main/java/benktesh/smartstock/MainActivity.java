package benktesh.smartstock;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import benktesh.smartstock.Model.Stock;
import benktesh.smartstock.UI.CommonUIHelper;
import benktesh.smartstock.UI.StockDetailActivity;
import benktesh.smartstock.Utils.MarketAdapter;
import benktesh.smartstock.Utils.NetworkUtilities;
import benktesh.smartstock.Utils.PortfolioAdapter;
import benktesh.smartstock.Utils.SmartStockConstant;

public class MainActivity extends AppCompatActivity implements
        MarketAdapter.ListItemClickListener, PortfolioAdapter.ListItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    CommonUIHelper mCommonUIHelper;
    private Toast mToast;

    //The following are for market summary
    private MarketAdapter mAdapter;
    private RecyclerView mMarketRV;
    ArrayList<Stock> mMarketData;

    //the following are for portfolio summary
    private PortfolioAdapter mStockAdapter;
    private RecyclerView mStockRV;
    ArrayList<Stock> mStockData;

    private ProgressBar spinner;

    private int AsyncTaskCount = 0;
    private int AsyncTaskRequested = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner = (ProgressBar)findViewById(R.id.progressbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (mCommonUIHelper == null) {
            mCommonUIHelper = new CommonUIHelper(this);
        }

        mMarketRV = findViewById(R.id.rv_market_summary);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mMarketRV.setLayoutManager(layoutManager);
        mMarketRV.setHasFixedSize(true);

        mAdapter = new MarketAdapter(mMarketData, this);
        mMarketRV.setAdapter(mAdapter);


        mStockRV = findViewById(R.id.rv_stocks);
        LinearLayoutManager layoutManager_portfolio = new LinearLayoutManager(this);
        mStockRV.setLayoutManager(layoutManager_portfolio);
        mStockRV.setHasFixedSize(true);
        mStockAdapter = new PortfolioAdapter(mStockData, this);
        mStockRV.setAdapter(mStockAdapter);

        LoadView();

    }

    private void LoadView() {
        Log.d(TAG, "Getting Market Data Async");

            AsyncTaskRequested = 3; //we are requesting two asynctasks
            new NetworkQueryTask().execute(SmartStockConstant.QueryPopulate);
            new NetworkQueryTask().execute(SmartStockConstant.QueryMarket);
            new NetworkQueryTask().execute(SmartStockConstant.QueryPortfolio);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        return mCommonUIHelper.ConfigureSearchFromMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mCommonUIHelper.MakeMenu(item)) return true;
        return super.onOptionsItemSelected(item);
    }


    private void ShowMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onListItemClick(Stock data) {
        if (mToast != null) {
            mToast.cancel();
        }
        String toastMessage = "Item #" + data.Symbol + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
        mToast.show();

        Intent intent = new Intent(this.getApplicationContext(), StockDetailActivity.class);
        intent.putExtra(SmartStockConstant.ParcelableStock, data);

        startActivity(intent);

    }

    /*
    This is an async task that fetches data from network and new data is applied to adapter.
    Also makes a long toast message when fails to retrieve information from the network
    It takes void, void and returns ArrayList<?>
     */
    class NetworkQueryTask extends AsyncTask<String, Void, ArrayList<Stock>> {

        private String query;
        @Override
        protected ArrayList<Stock> doInBackground(String... params) {
            AsyncTaskCount++;
            query = params[0];
            if(spinner != null ) {
                spinner.setVisibility(View.VISIBLE);
            }
            ArrayList<Stock> searchResults = null;
            try {
                if(query == SmartStockConstant.QueryMarket) {
                    searchResults = NetworkUtilities.getMarketData();

                    Log.d(TAG, query + ": Calling getMarketData() "  + " " + searchResults.size());
                }
                else if(query == SmartStockConstant.QueryPopulate) {

                    boolean result = NetworkUtilities.populateSymbol(getApplicationContext(), false);
                    Log.d(TAG, query + ": Calling poplate symabole(contxt, false) returned: " + result);

                }
                else {

                    searchResults = NetworkUtilities.getStockData(getApplicationContext(), query);
                    Log.d(TAG,  "Calling getStockData( " + query + ") " + searchResults.size());

                }

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(ArrayList<Stock> searchResults) {
            super.onPostExecute(searchResults);

            if(query != null) {
                if (searchResults != null && searchResults.size() != 0) {
                    if(query == SmartStockConstant.QueryMarket) {
                        mAdapter.resetData(searchResults);
                    }
                    else if(query == SmartStockConstant.QueryPortfolio) {
                        mStockAdapter.resetData(searchResults);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), R.string.Network_Error_Prompt, Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e(TAG, "onPostExecute: Query is Null in Async. Nothing Done");
            }

            if(AsyncTaskCount == AsyncTaskRequested && spinner != null && spinner.getVisibility() == View.VISIBLE) {
                spinner.setVisibility(View.GONE);
            }
        }
    }
}
