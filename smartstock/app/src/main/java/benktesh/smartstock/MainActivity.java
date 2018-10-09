package benktesh.smartstock;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


import android.widget.Toast;

import benktesh.smartstock.UI.CommonUIHelper;

public class MainActivity extends AppCompatActivity {

    CommonUIHelper mCommonUIHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if(mCommonUIHelper == null) {
            mCommonUIHelper = new CommonUIHelper(this);
        }

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
}
