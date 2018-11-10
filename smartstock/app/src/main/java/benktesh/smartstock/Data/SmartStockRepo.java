package benktesh.smartstock.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import benktesh.smartstock.Model.Portfolio;

public final class SmartStockRepo {

    private SQLiteDatabase mDb;
    private Context mContext;

    public SmartStockRepo(Context mContext) {
        SmartStrockDbHelper dbHelper = new SmartStrockDbHelper(mContext);
        mDb = dbHelper.getWritableDatabase();
    }

    public Cursor getAllPortfolio(int i) {
        return null;
    }

    public Cursor getPortfolio(int i) {
        return null;
    }

    public int savePortfolio(Portfolio portfolio) {

        return 0;

    }

    public Cursor getAllMarket(int i) {
        return null;
    }

    public Cursor getMarket(int i) {
        return null;
    }

    public int saveMarket(Portfolio portfolio) {

        return 0;

    }


}
