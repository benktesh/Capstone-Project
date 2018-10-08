package benktesh.smartstock.UI;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import benktesh.smartstock.R;
import benktesh.smartstock.SearchActivity;
import benktesh.smartstock.Utils.SmartStockConstant;

import static android.content.Intent.ACTION_SEARCH;

public class Common {

    Context mContext;

    private static String TAG = Common.class.getSimpleName();
    public Common(Context context) {
        mContext = context;
    }

    public boolean MakeMenu(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //TODO
            ShowMessage("Settings is shown");
            return true;
        }
        if (id == R.id.action_exit) {
            ShowMessage("Exiting SmartStock.");
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            return true;
        }
        if (id == R.id.action_portfolio) {
            //TODO
            ShowMessage("Portfolio is shown");
            Intent intent = new Intent(mContext, PortfolioActivity.class);
            mContext.startActivity(intent);
            return true;
        }
        if(id == R.id.action_search) {
            //TODO
            ShowMessage("Search is handled");

        }
        //TODO
        ShowMessage("TO BE DONE");
        return false;
    }

    public void ShowMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public boolean ConfigureSearchFromMenu(Menu menu) {
        // Inflate the options menu from XML
        Activity activity = (Activity) mContext;
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) mContext.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(500);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(activity.getComponentName()));

        return true;
    }

}
