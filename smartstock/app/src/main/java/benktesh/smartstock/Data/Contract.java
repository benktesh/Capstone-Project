package benktesh.smartstock.Data;

import android.provider.BaseColumns;

public class Contract {

    public static final class PortfolioEntry implements BaseColumns {
        public static final String TABLE_NAME = "portfolio";

        public static final String COLUMN_SYMBOL = "symbol";
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

        public static final String COLUMN_CHANGESYMBOL = "symbol";
        public static final String COLUMN_ = "change";
        public static final String COLUMN_VALUE = "value";
        public static final String COLUMN_DAY_HIGH = "dayHigh";
        public static final String COLUMN_DAY_LOW = "dayLow";
        public static final String COLUMN_TIMESTAMP = "timestamp";

    }
}
