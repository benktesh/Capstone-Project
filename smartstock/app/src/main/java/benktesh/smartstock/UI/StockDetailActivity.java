package benktesh.smartstock.UI;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import benktesh.smartstock.Model.StockDetail;
import benktesh.smartstock.R;

public class StockDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);


        StockDetail detail = new StockDetail("ABC", 2.0, true, "NYSE", 100.0, 99.0, 100.0 );




    }

}
