package benktesh.smartstock.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Trade implements Parcelable, Comparable {

    public static final Creator<Trade> CREATOR = new Creator<Trade>() {
        @Override
        public Trade createFromParcel(Parcel in) {
            return new Trade(in);
        }

        @Override
        public Trade[] newArray(int size) {
            return new Trade[size];
        }
    };
    public double price;
    public double size;
    public long timestamp;


    public Trade() {
    }

    public Trade(double p, double s, long t) {
        price = p;
        size = s;
        timestamp = t;
    }

    protected Trade(Parcel in) {
        price = in.readDouble();
        size = in.readDouble();
        timestamp = in.readLong();
    }

    public Date getTime() {
        try {

            java.util.Date time = new java.util.Date(timestamp);

            return time;
        } catch (Exception e) { //this generic but you can control other types of exception
            // look the origin of exception
        }

        return null;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(price);
        dest.writeDouble(size);
        dest.writeLong(timestamp);
    }

    @Override
    public int compareTo(Object o) {
        Trade e = (Trade) o;
        return this.timestamp < ((Trade) o).timestamp ? -1 :
                this.timestamp > ((Trade) o).timestamp ? 1 : 0;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Trade) obj).timestamp == timestamp;
    }


}
