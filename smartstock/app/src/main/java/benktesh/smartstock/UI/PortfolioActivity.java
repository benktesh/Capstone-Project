package benktesh.smartstock.UI;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import benktesh.smartstock.R;
import benktesh.smartstock.SearchActivity;

public class PortfolioActivity extends AppCompatActivity {

    private static String TAG = PortfolioActivity.class.getSimpleName();

    Common mCommon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if(mCommon == null)
        {
            mCommon = new Common(this);
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

}
