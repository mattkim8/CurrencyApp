package com.example.mattkim.currencyapp;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DatabaseInterface {

    @Query("SELECT * FROM RateRecord")
    List<RateRecord> getAllItems();
    //List<Earthquakes> getAllItems();

    @Insert
    void insertAll(RateRecord... eartquakeRecords);
    //void insertAll(EartquakeRecord... Earthquakes);

    @Query("DELETE FROM RateRecord")
    public void dropTheTable();

    //@Delete
    @Query("DELETE FROM RateRecord where id == :eqid")
    public void deleteItems(String eqid);

    @RawQuery
    int vacuumDb(SupportSQLiteQuery supportSQLiteQuery);
}