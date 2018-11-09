package benktesh.smartstock.Data;

import android.provider.BaseColumns;

public class SmartStockContract {

    public static final class PortfolioEntry implements BaseColumns {
        public static final String TABLE_NAME = "portfolio";

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
        public static final String TABLE_NAME = "market";
        public static final String COLUMN_SYMBOL = "symbol";
        public static final String COLUMN_CHANGE = "change";
        public static final String COLUMN_VALUE = "value";
        public static final String COLUMN_DAY_HIGH = "dayHigh";
        public static final String COLUMN_DAY_LOW = "dayLow";
        public static final String COLUMN_TIMESTAMP = "timestamp";

    }

    public static final class SymbolEntry implements BaseColumns {
        public static final String TABLE_NAME = "symbol";
        public static final String COLUMN_SYMBOL = "symbol";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_ISENABLED = "isenabled";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_IEXID = "iexid";
        public static final String COLUMN_TIMESTAMP = "timestamp";

    }

    public static final class UpdateEntry implements BaseColumns {
        public static final String TABLE_NAME = "audit";
        public static final String COLUMN_TABLE = "tablename";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIMESTAMP = "timestamp";

    }
}
