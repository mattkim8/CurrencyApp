package com.example.mattkim.currencyapp;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


@Dao
public interface EventDao {

    @Query("SELECT * FROM " + RateRecord2.TABLE_NAME )
    List<RateRecord2> getEvents();

    @Insert
    void addEvent(RateRecord2 event);

    @Delete
    void deleteEvent(RateRecord2 event);

    @Delete
    void deleteRate(LiveRate rate);

    @Update
    void updateEvent(RateRecord2 event);

    @Query("DELETE FROM " + RateRecord2.TABLE_NAME)
    public void dropTheTable();

}
