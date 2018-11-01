package benktesh.smartstock.UI;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import benktesh.smartstock.Model.Stock;
import benktesh.smartstock.Model.Trade;
import benktesh.smartstock.R;
import benktesh.smartstock.Utils.NetworkUtilities;
import benktesh.smartstock.Utils.SmartStockConstant;
import benktesh.smartstock.databinding.ActivityStockdetailBinding;


public class StockDetailActivity extends AppCompatActivity {

    private static String TAG = StockDetailActivity.class.getSimpleName();
    private Stock stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stock = getIntent().getParcelableExtra(SmartStockConstant.ParcelableStock);
        ActivityStockdetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_stockdetail);

        if(stock == null)
        {
            Toast.makeText(this, getString(R.string.Error_Loading_Detail), Toast.LENGTH_SHORT).show();
        }
        else {
            Log.d(TAG, "Stock " + stock.Symbol);


            //TODO replace data point with real
            GraphView graph = (GraphView) findViewById(R.id.graph_stock_detail);

            DataPoint[] dp = new DataPoint[stock.Trades.size()];

            Collections.sort(stock.Trades);
            for(int i = 0; i < stock.Trades.size(); i++) {
                Trade t = stock.Trades.get(i);
                dp[i] = new DataPoint(t.timestamp, t.price);
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
            graph.addSeries(series);
            binding.setStock(stock);


            if(stock.InPortoflio){
                //display portfolio specific UIs

                Toast.makeText(this, "Is Portfolio ", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(this, "Not In Portfolio ", Toast.LENGTH_SHORT).show();

            }

        }



    }

    public void onTogglePortfolio(View view) {
        if(((ToggleButton) view).isChecked()) {
            Toast.makeText(this, "Add to Portfolio ", Toast.LENGTH_SHORT).show();
            new AddPortFolioTask().execute(stock.Symbol);

        } else {
            Toast.makeText(this, "Remove from Portfolio ", Toast.LENGTH_SHORT).show();
            new RemovePortFolioTask().execute(stock.Symbol);

        }
    }

    class AddPortFolioTask extends AsyncTask<String, Void, Boolean> {

        private String query;

        @Override
        protected Boolean doInBackground(String... params) {
            query = params[0];


            ArrayList<Stock> searchResults = null;
            try {

                return NetworkUtilities.addPortfolio(getApplicationContext(), query);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return true;
        }

    }

    class RemovePortFolioTask extends AsyncTask<String, Void, Boolean> {

        private String query;

        @Override
        protected Boolean doInBackground(String... params) {
            query = params[0];
            try {

                return NetworkUtilities.removePortfolio(getApplicationContext(), query);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
                return false;
            }
        }

    }

}
