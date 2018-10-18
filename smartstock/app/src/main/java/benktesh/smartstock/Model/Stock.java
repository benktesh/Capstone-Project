package benktesh.smartstock.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Stock implements Parcelable{

    public  String Symbol;
    public  Double Change;
    public boolean InPortoflio;
    public String Market;
    public Double DayHigh;
    public Double DayLow;
    public Double Price;
    public boolean IsMarket;




    public Stock() {

    }


    public Stock(String Symbol, Double Change, boolean InPortoflio, String Market, Double DayHigh,
                 Double DayLow, Double Price, boolean IsMarket) {
        this.Symbol = Symbol;
        this.Change = Change;
        this.InPortoflio = InPortoflio;
        this.Market = Market;
        this.DayHigh = DayHigh;
        this.DayLow = DayLow;
        this.Price = Price;
        this.IsMarket = IsMarket;
    }

    protected Stock(Parcel in) {
        Symbol = in.readString();
        if (in.readByte() == 0) {
            Change = null;
        } else {
            Change = in.readDouble();
        }
        InPortoflio = in.readByte() != 0;
        Market = in.readString();
        if (in.readByte() == 0) {
            DayHigh = null;
        } else {
            DayHigh = in.readDouble();
        }
        if (in.readByte() == 0) {
            DayLow = null;
        } else {
            DayLow = in.readDouble();
        }
        if (in.readByte() == 0) {
            Price = null;
        } else {
            Price = in.readDouble();
        }
        IsMarket = in.readByte() != 0;
    }

    public static final Creator<Stock> CREATOR = new Creator<Stock>() {
        @Override
        public Stock createFromParcel(Parcel in) {
            return new Stock(in);
        }

        @Override
        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Symbol);
        if (Change == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(Change);
        }
        dest.writeByte((byte) (InPortoflio ? 1 : 0));
        dest.writeString(Market);
        if (DayHigh == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(DayHigh);
        }
        if (DayLow == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(DayLow);
        }
        if (Price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(Price);
        }
        dest.writeByte((byte) (IsMarket ? 1 : 0));

    }

}
