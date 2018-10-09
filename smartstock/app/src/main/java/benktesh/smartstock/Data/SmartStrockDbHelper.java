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

        final String SQL_CREATE_PORTFOLIO_TABLE = "CREATE TABLE " + Contract.PortfolioEntry.TABLE_NAME + " (" +
                Contract.PortfolioEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Contract.PortfolioEntry.COLUMN_SYMBOL + " STRING NOT NULL, " +
                Contract.PortfolioEntry.COLUMN_MARKET + " STRING , " +
                Contract.PortfolioEntry.COLUMN_PRICE + " DOUBLE DEFAULT 0, " +
                Contract.PortfolioEntry.COLUMN_CHANGE + " DOUBLE DEFAULT 0, " +
                Contract.PortfolioEntry.COLUMN_DAY_HIGH + " DOUBLE DEFAULT 0 , " +
                Contract.PortfolioEntry.COLUMN_DAY_LOW + " DOUBLE DEFAULT 0, " +
                Contract.PortfolioEntry.COLUMN_INPORTFOLIO + " INTEGER DEFAULT 1,  " +
                Contract.PortfolioEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";
        db.execSQL(SQL_CREATE_PORTFOLIO_TABLE);

        final String SQL_CREATE_MARKET_TABLE = "CREATE TABLE " + Contract.MarketEntry.TABLE_NAME + " (" +
                Contract.MarketEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Contract.MarketEntry.COLUMN_SYMBOL + " STRING NOT NULL, " +
                Contract.MarketEntry.COLUMN_CHANGE + " STRING DEFAULT 0, " +
                Contract.MarketEntry.COLUMN_VALUE + " DOUBLE DEFAULT 0, " +
                Contract.MarketEntry.COLUMN_DAY_HIGH + " DOUBLE DEFAULT 0, " +
                Contract.MarketEntry.COLUMN_DAY_LOW + " DOUBLE DEFAULT 0, " +
                Contract.MarketEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";
        db.execSQL(SQL_CREATE_MARKET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(TAG, "Upgrading database to " + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + Contract.PortfolioEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.MarketEntry.TABLE_NAME);

        onCreate(db);
    }
}
