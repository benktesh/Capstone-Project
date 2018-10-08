package benktesh.smartstock.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class StockDetail implements Parcelable{

    public  String mSymbol;
    public  Double mChange;
    public boolean mInPortoflio;
    public String mMarket;
    public Double mDayHigh;
    public Double mDayLow;
    public Double mPrice;

    public StockDetail() {

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
