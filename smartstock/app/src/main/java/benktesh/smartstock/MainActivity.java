package benktesh.smartstock;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import benktesh.smartstock.Model.Market;
import benktesh.smartstock.Model.Stock;
import benktesh.smartstock.UI.CommonUIHelper;
import benktesh.smartstock.UI.StockDetailActivity;
import benktesh.smartstock.Utils.MarketAdapter;
import benktesh.smartstock.Utils.NetworkUtilities;
import benktesh.smartstock.Utils.PortfolioAdapter;

public class MainActivity extends AppCompatActivity implements
        MarketAdapter.ListItemClickListener, PortfolioAdapter.ListItemClickListener {

    CommonUIHelper mCommonUIHelper;
    private Toast mToast;

    //The following are for market summary
    private MarketAdapter mAdapter;
    private RecyclerView mMarketRV;
    ArrayList<Market> mData;

    //the following are for portfolio summary
    private PortfolioAdapter mStockAdapter;
    private RecyclerView mStockRV;
    ArrayList<Stock> mStockData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        mAdapter = new MarketAdapter(mData, this);
        mMarketRV.setAdapter(mAdapter);

        mData = NetworkUtilities.getMarketData();
        mAdapter.resetData(mData);



        mStockRV = findViewById(R.id.rv_stocks);
        LinearLayoutManager layoutManager_portfolio = new LinearLayoutManager(this);
        mStockRV.setLayoutManager(layoutManager_portfolio);
        mStockRV.setHasFixedSize(true);

        mStockAdapter = new PortfolioAdapter(mStockData, this);
        mStockRV.setAdapter(mStockAdapter);

        mStockData = NetworkUtilities.getStockData(null);
        mStockAdapter.resetData(mStockData);

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
    public void onListItemClick(Market market) {
        if (mToast != null) {
            mToast.cancel();
        }
        String toastMessage = "Item #" + market.Symbol + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
        mToast.show();

        Intent startChildActivityIntent = new Intent(this.getApplicationContext(), StockDetailActivity.class);
        startActivity(startChildActivityIntent);
    }

    @Override
    public void onListItemClick(Stock data) {
        if (mToast != null) {
            mToast.cancel();
        }
        String toastMessage = "Item #" + data.Symbol + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
        mToast.show();

        Intent startChildActivityIntent = new Intent(this.getApplicationContext(), StockDetailActivity.class);
        startActivity(startChildActivityIntent);

    }
}
