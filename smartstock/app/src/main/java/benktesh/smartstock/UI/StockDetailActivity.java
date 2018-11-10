package benktesh.smartstock.UI;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import benktesh.smartstock.Model.Chart;
import benktesh.smartstock.Model.Stock;
import benktesh.smartstock.R;
import benktesh.smartstock.Utils.NetworkUtilities;
import benktesh.smartstock.Utils.SmartStockConstant;
import benktesh.smartstock.databinding.ActivityStockdetailBinding;


public class StockDetailActivity extends AppCompatActivity {

    private static String TAG = StockDetailActivity.class.getSimpleName();
    private Stock stock;

    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stock = getIntent().getParcelableExtra(SmartStockConstant.ParcelableStock);
        ActivityStockdetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_stockdetail);

        if (stock == null) {
            Toast.makeText(this, getString(R.string.Error_Loading_Detail), Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "Stock " + stock.Symbol);

            //load chart only when there is data
            if (stock.Charts != null) {

                GraphView graph = findViewById(R.id.graph_stock_detail);

                DataPoint[] dp = new DataPoint[stock.Charts.size()];

                Collections.sort(stock.Charts);
                for (int i = 0; i < stock.Charts.size(); i++) {
                    Chart d = stock.Charts.get(i);
                    dp[i] = new DataPoint(d.getDateForChart(), (double) d.Average);
                }

                Date date = new Date();


                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
                graph.addSeries(series);

                graph.getGridLabelRenderer().setLabelFormatter(
                        new DefaultLabelFormatter() {
                            @Override
                            public String formatLabel(double value, boolean isValuex) {
                                if (isValuex) {
                                    return sdf.format(new Date((long) value));
                                } else {
                                    return super.formatLabel(value, isValuex);
                                }
                            }
                        }
                );

                //graph.getGridLabelRenderer().setNumHorizontalLabels(9);
            }

            if (stock.IsMarket) {
                //remove the view related to portforlio for market stocks
                View portfolio = findViewById(R.id.stockdetail_portfolio);
                portfolio.setVisibility(View.GONE);
            }
            binding.setStock(stock);
        }
    }

    public void onTogglePortfolio(View view) {
        if (((ToggleButton) view).isChecked()) {
            //Toast.makeText(this, "Add to Portfolio ", Toast.LENGTH_SHORT).show();
            new AddPortFolioTask().execute(stock.Symbol);

        } else {
            //Toast.makeText(this, "Remove from Portfolio ", Toast.LENGTH_SHORT).show();
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
