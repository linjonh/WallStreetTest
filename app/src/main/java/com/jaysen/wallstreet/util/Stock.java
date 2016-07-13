package com.jaysen.wallstreet.util;

import android.os.Parcel;
import android.os.Parcelable;

public class Stock implements Parcelable {
    public static final String TRADE = "TRADE";//正常交易
    public static final String HALT = "HALT";//停牌
    public static final String BREAK = "BREAK";//休市
    public static final String ENDTR = "ENDTR";//收盘
    public static final String OCALL = "OCALL";//集合竞价(09:15 --09:30)
    public String id;
    public String name;
    public String symbol;
    public boolean isFav;//是否收藏
    public String px_change;//涨跌额
    public String last_px;//最新价格 现价
    public String px_change_rate;//涨跌幅
    public String trade_status = HALT;  //交易状态 "TRADE"=>正常交易  "HALT"=>停牌

    //public String desc;
    public Stock() {
    }

    protected Stock(Parcel in) {
        id = in.readString();
        name = in.readString();
        symbol = in.readString();
        isFav = in.readByte() != 0;
        px_change = in.readString();
        last_px = in.readString();
        px_change_rate = in.readString();
        trade_status = in.readString();
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
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(symbol);
        dest.writeByte((byte) (isFav ? 1 : 0));
        dest.writeString(px_change);
        dest.writeString(last_px);
        dest.writeString(px_change_rate);
        dest.writeString(trade_status);
    }

}
