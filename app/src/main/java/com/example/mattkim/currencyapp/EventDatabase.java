package com.example.mattkim.currencyapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(entities = {RateRecord2.class, LiveRate.class},version = 1)
public abstract class EventDatabase extends RoomDatabase{
    private static final String DB_NAME = "Event_Database.db";
    private static EventDatabase INSTANCE;
    public abstract EventDao eventDao();
    public static EventDatabase getEventDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, EventDatabase.class, DB_NAME).build();

        }
        return INSTANCE;
    }
}
