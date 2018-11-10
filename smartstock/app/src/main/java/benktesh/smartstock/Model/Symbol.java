package benktesh.smartstock.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Symbol implements Parcelable {
    public static final Creator<Symbol> CREATOR = new Creator<Symbol>() {
        @Override
        public Symbol createFromParcel(Parcel in) {
            return new Symbol(in);
        }

        @Override
        public Symbol[] newArray(int size) {
            return new Symbol[size];
        }
    };
    public String symbol;
    public String name;
    public String date;
    public String isenabled;
    public String type;
    public String iexid;

    public Symbol() {

    }

    protected Symbol(Parcel in) {
        symbol = in.readString();
        name = in.readString();
        date = in.readString();
        isenabled = in.readString();
        type = in.readString();
        iexid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(symbol);
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(isenabled);
        dest.writeString(type);
        dest.writeString(iexid);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
