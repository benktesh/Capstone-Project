package benktesh.smartstock.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class StockDetail implements Parcelable{
    private String mSymbol;
    private Double mChange;
    private boolean mInPortoflio;
    private String mMarket;
    private Double mDayHigh;
    private Double mDayLow;
    private Double mPrice;

    public String getmSymbol() {
        return mSymbol;
    }

    public void setmSymbol(String mSymbol) {
        this.mSymbol = mSymbol;
    }

    public Double getmChange() {
        return mChange;
    }

    public void setmChange(Double mChange) {
        this.mChange = mChange;
    }

    public boolean ismInPortoflio() {
        return mInPortoflio;
    }

    public void setmInPortoflio(boolean mInPortoflio) {
        this.mInPortoflio = mInPortoflio;
    }

    public String getmMarket() {
        return mMarket;
    }

    public void setmMarket(String mMarket) {
        this.mMarket = mMarket;
    }

    public Double getmDayHigh() {
        return mDayHigh;
    }

    public void setmDayHigh(Double mDayHigh) {
        this.mDayHigh = mDayHigh;
    }

    public Double getmDayLow() {
        return mDayLow;
    }

    public void setmDayLow(Double mDayLow) {
        this.mDayLow = mDayLow;
    }

    public Double getmPrice() {
        return mPrice;
    }

    public void setmPrice(Double mPrice) {
        this.mPrice = mPrice;
    }

    public StockDetail(String mSymbol, Double mChange, boolean mInPortoflio, String mMarket, Double mDayHigh, Double mDayLow, Double mPrice) {
        this.mSymbol = mSymbol;
        this.mChange = mChange;
        this.mInPortoflio = mInPortoflio;
        this.mMarket = mMarket;
        this.mDayHigh = mDayHigh;
        this.mDayLow = mDayLow;
        this.mPrice = mPrice;
    }

    protected StockDetail(Parcel in) {
        mSymbol = in.readString();
        if (in.readByte() == 0) {
            mChange = null;
        } else {
            mChange = in.readDouble();
        }
        mInPortoflio = in.readByte() != 0;
        mMarket = in.readString();
        if (in.readByte() == 0) {
            mDayHigh = null;
        } else {
            mDayHigh = in.readDouble();
        }
        if (in.readByte() == 0) {
            mDayLow = null;
        } else {
            mDayLow = in.readDouble();
        }
        if (in.readByte() == 0) {
            mPrice = null;
        } else {
            mPrice = in.readDouble();
        }
    }

    public static final Creator<StockDetail> CREATOR = new Creator<StockDetail>() {
        @Override
        public StockDetail createFromParcel(Parcel in) {
            return new StockDetail(in);
        }

        @Override
        public StockDetail[] newArray(int size) {
            return new StockDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSymbol);
        if (mChange == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(mChange);
        }
        dest.writeByte((byte) (mInPortoflio ? 1 : 0));
        dest.writeString(mMarket);
        if (mDayHigh == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(mDayHigh);
        }
        if (mDayLow == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(mDayLow);
        }
        if (mPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(mPrice);
        }
    }
}
