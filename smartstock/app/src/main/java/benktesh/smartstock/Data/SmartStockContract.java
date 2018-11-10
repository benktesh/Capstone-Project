package benktesh.smartstock.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public class SmartStockContract {

    public static final String AUTHORITY = "benktesh.smartstock";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_PORTFOLIO = "portfolio";
    public static final String PATH_MARKET = "market";
    public static final String PATH_SYMBOL = "symbol";
    public static final String PATH_AUDIT = "audit";


    public SmartStockContract() {
    }

    public static final class PortfolioEntry implements BaseColumns {

        public static final Uri PORTFOLIO_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PORTFOLIO).build();

        public static final String TABLE_NAME = PATH_PORTFOLIO;

        public static final String COLUMN_SYMBOL = "symbol";
        public static final String COLUMN_POSITION = "position";
        public static final String COLUMN_CHANGE = "change";
        public static final String COLUMN_INPORTFOLIO = "inPortfolio";
        public static final String COLUMN_MARKET = "market";
        public static final String COLUMN_DAY_HIGH = "dayHigh";
        public static final String COLUMN_DAY_LOW = "dayLow";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_TIMESTAMP = "timestamp";

    }

    public static final class MarketEntry implements BaseColumns {

        public static final Uri MARKET_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MARKET).build();

        public static final String TABLE_NAME = PATH_MARKET;
        public static final String COLUMN_SYMBOL = "symbol";
        public static final String COLUMN_CHANGE = "change";
        public static final String COLUMN_VALUE = "value";
        public static final String COLUMN_DAY_HIGH = "dayHigh";
        public static final String COLUMN_DAY_LOW = "dayLow";
        public static final String COLUMN_TIMESTAMP = "timestamp";

    }

    public static final class SymbolEntry implements BaseColumns {

        public static final Uri SYMBOL_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SYMBOL).build();

        public static final String TABLE_NAME = PATH_SYMBOL;
        public static final String COLUMN_SYMBOL = "symbol";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_ISENABLED = "isenabled";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_IEXID = "iexid";
        public static final String COLUMN_TIMESTAMP = "timestamp";

    }

    public static final class AuditEntry implements BaseColumns {

        public static final Uri AUDIT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_AUDIT).build();
        public static final String TABLE_NAME = PATH_AUDIT;
        public static final String COLUMN_TABLE = "tablename";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIMESTAMP = "timestamp";

    }
}
