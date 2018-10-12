package benktesh.smartstock;

import android.util.Log;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import benktesh.smartstock.Model.Stock;
import benktesh.smartstock.Utils.LibraryHelper;
import benktesh.smartstock.Utils.SmartStockConstant;

import static org.junit.Assert.*;

public class LibraryHelperTest {

    @Test
    public void trimTrimsTheData() {
        ArrayList<Stock> searchResult = null;


        Assert.assertEquals(searchResult, null);
        LibraryHelper.Trim(searchResult, 2);
        Assert.assertNull(searchResult);

        searchResult = new ArrayList<>();

        Assert.assertTrue("Size must be >=0", searchResult.size() >= 0);
        LibraryHelper.Trim(searchResult, 2);
        Assert.assertTrue("Size must be zero", searchResult.size() == 0);

        //return data
        searchResult.add(new Stock("EGOV", 1.0,
                false, "NASDAQ", 100.0, 99.0, 100.0, false));
        searchResult.add(new Stock("SPY", 1.0,
                true, "NYSE", 100.0, 99.0, 100.0, false));

        searchResult.add(new Stock("ARR", 1.0,
                false, "NYSE", 100.0, 99.0, 100.0, false));

        searchResult.add(new Stock("GE", 1.0,
                true, "NYSE", 100.0, 99.0, 100.0, false));

        searchResult.add(new Stock("SPY", 1.0,
                true, "NYSE", 100.0, 99.0, 100.0, false));

        Assert.assertTrue(searchResult.size() > 2);
        LibraryHelper.Trim(searchResult, 2);
        Assert.assertTrue(searchResult.size() == 2);
    }
}