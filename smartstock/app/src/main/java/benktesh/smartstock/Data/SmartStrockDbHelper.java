package benktesh.smartstock.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SmartStrockDbHelper extends SQLiteOpenHelper {

    private static final String TAG = SmartStrockDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "smartstock.db";

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    private static final int DATABASE_VERSION = 1;

    public SmartStrockDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_PORTFOLIO_TABLE = "CREATE TABLE " + SmartStockContract.PortfolioEntry.TABLE_NAME + " (" +
                SmartStockContract.PortfolioEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SmartStockContract.PortfolioEntry.COLUMN_SYMBOL + " STRING NOT NULL, " +
                SmartStockContract.PortfolioEntry.COLUMN_MARKET + " STRING , " +
                SmartStockContract.PortfolioEntry.COLUMN_PRICE + " DOUBLE DEFAULT 0, " +
                SmartStockContract.PortfolioEntry.COLUMN_CHANGE + " DOUBLE DEFAULT 0, " +
                SmartStockContract.PortfolioEntry.COLUMN_DAY_HIGH + " DOUBLE DEFAULT 0 , " +
                SmartStockContract.PortfolioEntry.COLUMN_DAY_LOW + " DOUBLE DEFAULT 0, " +
                SmartStockContract.PortfolioEntry.COLUMN_INPORTFOLIO + " INTEGER DEFAULT 1,  " +
                SmartStockContract.PortfolioEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";
        db.execSQL(SQL_CREATE_PORTFOLIO_TABLE);

        final String SQL_CREATE_MARKET_TABLE = "CREATE TABLE " + SmartStockContract.MarketEntry.TABLE_NAME + " (" +
                SmartStockContract.MarketEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SmartStockContract.MarketEntry.COLUMN_SYMBOL + " STRING NOT NULL, " +
                SmartStockContract.MarketEntry.COLUMN_CHANGE + " STRING DEFAULT 0, " +
                SmartStockContract.MarketEntry.COLUMN_VALUE + " DOUBLE DEFAULT 0, " +
                SmartStockContract.MarketEntry.COLUMN_DAY_HIGH + " DOUBLE DEFAULT 0, " +
                SmartStockContract.MarketEntry.COLUMN_DAY_LOW + " DOUBLE DEFAULT 0, " +
                SmartStockContract.MarketEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";
        db.execSQL(SQL_CREATE_MARKET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(TAG, "Upgrading database to " + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + SmartStockContract.PortfolioEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SmartStockContract.MarketEntry.TABLE_NAME);

        onCreate(db);
    }
}
