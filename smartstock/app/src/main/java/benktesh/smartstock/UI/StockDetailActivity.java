package benktesh.smartstock.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import benktesh.smartstock.Model.Stock;
import benktesh.smartstock.R;

public class StockDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);


        Stock detail = new Stock("ABC", 2.0, true, "NYSE", 100.0, 99.0, 100.0 );




    }

}
