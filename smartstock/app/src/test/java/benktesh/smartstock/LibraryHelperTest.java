package benktesh.smartstock;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import benktesh.smartstock.Model.Chart;
import benktesh.smartstock.Model.Stock;
import benktesh.smartstock.Model.Trade;
import benktesh.smartstock.Utils.LibraryHelper;

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

    @Test
    public void timeStampConverion() {
        Trade t = new Trade();
        t.timestamp = Long.parseLong("1541016446379");

        Date d = t.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        Assert.assertTrue(cal.get(Calendar.YEAR) == 2018);


    }

    @Test
    public void chartMinuteFormatting() {
        Chart c = new Chart();
        c.Date = "20181107";
        c.Minute = "9:30";
        Date d = c.getDateForChart();
        Assert.assertTrue(d != null);

    }
}