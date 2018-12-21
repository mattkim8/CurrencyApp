package com.example.mattkim.currencyapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static com.example.mattkim.currencyapp.RateRecord2.TABLE_NAME;

//import static com.example.mattkim.currencyapp.RateRecord.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class RateRecord2 implements Serializable {

    public static final String TABLE_NAME = "wallet";

    @PrimaryKey(autoGenerate=true)
    public int id;

    @ColumnInfo
    public String exchange;

    @ColumnInfo
    public String rate;



    public RateRecord2(){

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "RateRecord{" +
                "id=" + id +
                ", exchange='" + exchange + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }
}
