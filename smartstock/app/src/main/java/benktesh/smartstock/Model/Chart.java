package benktesh.smartstock.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Chart implements Parcelable, Comparable {
    public String Date;
    public String Minute;
    public double High;
    public double Low;
    public double Volume;

    protected Chart(Parcel in) {
        Date = in.readString();
        Minute = in.readString();
        High = in.readDouble();
        Low = in.readDouble();
        Volume = in.readDouble();
        Average = in.readLong();
        Open = in.readDouble();
        Close = in.readDouble();
        MarketHigh = in.readDouble();
        MarketLow = in.readDouble();
        MarketOpen = in.readDouble();
        MarketClose = in.readDouble();
    }

    public static final Creator<Chart> CREATOR = new Creator<Chart>() {
        @Override
        public Chart createFromParcel(Parcel in) {
            return new Chart(in);
        }

        @Override
        public Chart[] newArray(int size) {
            return new Chart[size];
        }
    };

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getMinute() {
        return Minute;
    }

    public void setMinute(String minute) {
        Minute = minute;
    }

    public double getHigh() {
        return High;
    }

    public void setHigh(double high) {
        High = high;
    }

    public double getLow() {
        return Low;
    }

    public void setLow(double low) {
        Low = low;
    }

    public double getVolume() {
        return Volume;
    }

    public void setVolume(double volume) {
        Volume = volume;
    }

    public long getAverage() {
        return Average;
    }

    public void setAverage(long average) {
        Average = average;
    }

    public double getOpen() {
        return Open;
    }

    public void setOpen(double open) {
        Open = open;
    }

    public double getClose() {
        return Close;
    }

    public void setClose(double close) {
        Close = close;
    }

    public double getMarketHigh() {
        return MarketHigh;
    }

    public void setMarketHigh(double marketHigh) {
        MarketHigh = marketHigh;
    }

    public double getMarketLow() {
        return MarketLow;
    }

    public void setMarketLow(double marketLow) {
        MarketLow = marketLow;
    }

    public double getMarketOpen() {
        return MarketOpen;
    }

    public void setMarketOpen(double marketOpen) {
        MarketOpen = marketOpen;
    }

    public double getMarketClose() {
        return MarketClose;
    }

    public void setMarketClose(double marketClose) {
        MarketClose = marketClose;
    }

    public long Average;
    public double Open;
    public double Close;
    public double MarketHigh;
    public double MarketLow;
    public double MarketOpen;
    public double MarketClose;

    public Chart() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Date);
        dest.writeString(Minute);
        dest.writeDouble(High);
        dest.writeDouble(Low);
        dest.writeDouble(Volume);
        dest.writeLong(Average);
        dest.writeDouble(Open);
        dest.writeDouble(Close);
        dest.writeDouble(MarketHigh);
        dest.writeDouble(MarketLow);
        dest.writeDouble(MarketOpen);
        dest.writeDouble(MarketClose);
    }
    public Date getDateForChart() {

        DateFormat sdf = new SimpleDateFormat("yyyyMMdd:hh:mm");

        String yyyyMMddDatePattern = "yyyyMMdd";



        try {
            String date = (Date != null ? Date : String.format(yyyyMMddDatePattern, new Date()) )+ ":" + Minute;
            Date dateForChart = sdf.parse(date);
            return dateForChart;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public int compareTo(Object o) {
        Chart e = (Chart) o;
        return this.getDate().compareTo(((Chart) o).getDate());
    }

    @Override
    public boolean equals(Object obj) {
        return ((Chart) obj).getDate() == getDate();
    }


}
