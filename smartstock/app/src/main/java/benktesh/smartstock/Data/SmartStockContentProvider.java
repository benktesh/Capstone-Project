package benktesh.smartstock.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class SmartStockContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    SmartStrockDbHelper dbHelper;

    public static final int SYMBOLS = 100;
    public static final int SYMBOL_ID = 101;

    public static final int MARKETS = 110;
    public static final int MARKET_ID = 111;

    public static final int PORTFOLIOS = 120;
    public static final int PORTFOLIO_ID = 121;

    public static final int AUDITS = 130;
    public static final int AUDIT_ID = 131;

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(SmartStockContract.AUTHORITY, SmartStockContract.PATH_SYMBOL, SYMBOLS);
        uriMatcher.addURI(SmartStockContract.AUTHORITY, SmartStockContract.PATH_SYMBOL + "/*", SYMBOL_ID);

        uriMatcher.addURI(SmartStockContract.AUTHORITY, SmartStockContract.PATH_MARKET, MARKETS);
        uriMatcher.addURI(SmartStockContract.AUTHORITY, SmartStockContract.PATH_MARKET + "/*", MARKET_ID);

        uriMatcher.addURI(SmartStockContract.AUTHORITY, SmartStockContract.PATH_PORTFOLIO, PORTFOLIOS);
        uriMatcher.addURI(SmartStockContract.AUTHORITY, SmartStockContract.PATH_PORTFOLIO + "/*", PORTFOLIO_ID);

        uriMatcher.addURI(SmartStockContract.AUTHORITY, SmartStockContract.PATH_AUDIT, AUDITS);
        uriMatcher.addURI(SmartStockContract.AUTHORITY, SmartStockContract.PATH_AUDIT + "/*", AUDIT_ID);


        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new SmartStrockDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match) {
            case PORTFOLIOS:
                retCursor = db.query(SmartStockContract.PortfolioEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        SmartStockContract.PortfolioEntry.COLUMN_TIMESTAMP);
                break;
            case MARKETS:
                retCursor = db.query(SmartStockContract.MarketEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        SmartStockContract.MarketEntry.COLUMN_TIMESTAMP);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case PORTFOLIOS:
                long id = db.insert(SmartStockContract.PortfolioEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(SmartStockContract.PortfolioEntry.PORTFOLIO_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int deletedCount; // starts as 0

        switch (match) {
            case SYMBOL_ID:
                String id = uri.getPathSegments().get(1);
                deletedCount = db.delete(SmartStockContract.SymbolEntry.TABLE_NAME, "id=?", new String[]{id});
                break;
            case SYMBOLS: //delete all
                deletedCount = db.delete(SmartStockContract.SymbolEntry.TABLE_NAME, null, null);
                break;
            case MARKETS:
                deletedCount = db.delete(SmartStockContract.MarketEntry.TABLE_NAME, null, null);
                break;

            case PORTFOLIOS:
                deletedCount = db.delete(SmartStockContract.PortfolioEntry.TABLE_NAME, null, null);
                break;

            case AUDITS:
                deletedCount = db.delete(SmartStockContract.AuditEntry.TABLE_NAME, null, null);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (deletedCount != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedCount;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
