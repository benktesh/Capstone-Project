package benktesh.smartstock;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import benktesh.smartstock.UI.Common;
import benktesh.smartstock.UI.StockDetailActivity;
import benktesh.smartstock.Utils.SmartStockConstant;

public class SearchActivity extends AppCompatActivity implements SearchAdapter.ListItemClickListener {

    private static String TAG = SearchActivity.class.getSimpleName();

    Common mCommon;

    ArrayList<SearchRow> mData;


    /*
     * References to RecyclerView and Adapter to reset the list to its
     * "pretty" state when the reset menu item is clicked.
     */
    private SearchAdapter mAdapter;
    private RecyclerView mSearchList;
    private Toast mToast;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if(mCommon == null)
        {
            mCommon = new Common(this);
        }

        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mSearchList = (RecyclerView) findViewById(R.id.rv_stocks);

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

        // COMPLETE (13) Pass in this as the ListItemClickListener to the GreenAdapter constructor
        /*
         * The GreenAdapter is responsible for displaying each item in the list.
         */
        //mData = getSearchResult("hhh");
        mAdapter = new SearchAdapter(mData, this) ;
        mSearchList.setAdapter(mAdapter);



        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            //TODO this will move to async task
            mData = getSearchResult(query);
            mAdapter.resetData(mData);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
       return mCommon.ConfigureSearchFromMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mCommon.MakeMenu(item)) return true;
        return super.onOptionsItemSelected(item);

    }

    private ArrayList<SearchRow> getSearchResult(String query)
    {
        Log.d(TAG, "getSearchResult: " + query);
        ArrayList<SearchRow> searchResult;
        searchResult = new ArrayList<>();
        if(query.equals(SmartStockConstant.PortfolioQueryString)) {
            //TODO
            //Get portfolio
            //call latest data from server
            //save portfolio
            //return data
            searchResult.add(new SearchRow(1, "ABC", 1.0, "PortFolio1"));
            searchResult.add(new SearchRow(2, "ABC", 0.0, "PortFolio2"));
        }
        else {
            searchResult.add(new SearchRow(1, "ABC", 0.0, ""));
            searchResult.add(new SearchRow(2, "ABC", -1.0, ""));
            searchResult.add(new SearchRow(3, "ABC", -2.0, ""));
        }
        return searchResult;

    }


    private void ShowMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListItemClick(SearchRow stock) {
        if(mToast != null){
            mToast.cancel();
        }
        String toastMessage = "Item #" + stock.getSymbol() + " clicked.";
        mToast = Toast.makeText(this,toastMessage, Toast.LENGTH_LONG);
        mToast.show();




        Intent startChildActivityIntent = new Intent(this.getApplicationContext(), StockDetailActivity.class);
        startActivity(startChildActivityIntent);
    }
}
