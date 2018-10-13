package benktesh.smartstock.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

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

        mData = NetworkUtilities.getStockData(SmartStockConstant.PortfolioQueryString);
        mAdapter.resetData(mData);


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
}
