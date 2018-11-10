package benktesh.smartstock.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Portfolio implements Parcelable {
    public static final Creator<Portfolio> CREATOR = new Creator<Portfolio>() {
        @Override
        public Portfolio createFromParcel(Parcel in) {
            return new Portfolio(in);
        }

        @Override
        public Portfolio[] newArray(int size) {
            return new Portfolio[size];
        }
    };
    public String Symbol;
    public String Value;
    public String Market;
    public String DayHigh;
    public String DayLow;
    public String _Id;

    protected Portfolio(Parcel in) {
        Symbol = in.readString();
        Value = in.readString();
        Market = in.readString();
        DayHigh = in.readString();
        DayLow = in.readString();
        _Id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Symbol);
        dest.writeString(Value);
        dest.writeString(Market);
        dest.writeString(DayHigh);
        dest.writeString(DayLow);
        dest.writeString(_Id);
    }
}
