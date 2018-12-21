package com.example.mattkim.currencyapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Ignore;

import java.io.Serializable;

//import static com.example.mattkim.currencyapp.LiveRate.TABLE_NAME;

@Entity//(tableName = TABLE_NAME)
public class LiveRate implements Serializable{


  //  public static final String TABLE_NAME = "liverates";


    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private String exchange;
    @ColumnInfo
    private String rate;

    public LiveRate(){

    }
    @Ignore
    public LiveRate(int id, String rate, String exchange){
        this.id = id;
        this.rate = rate;
        this.exchange = exchange;
    }

    public String getExchange() {
        return exchange;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return "LiveRate{" +
                "exchange='" + exchange + '\'' +
                ", rate=" + rate +
                '}';
    }
}
