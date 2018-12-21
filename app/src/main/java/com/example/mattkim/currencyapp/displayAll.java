package com.example.mattkim.currencyapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class displayAll extends AsyncTask<Void,Void,List<RateRecord>>{
    // Variables for the search input field, and results TextViews
    private AppDatabase db;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public Context context;  //done as a hack to get app context
    double mag;
    List<RateRecord> items;

    // Constructor providing a reference to the views in MainActivity
    public displayAll(AppDatabase db, RecyclerView recyclerView, RecyclerView.Adapter adapter, Context context ) {
        this.db = db;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.context = context;
    }


    @Override
    protected List<RateRecord> doInBackground(Void... m) {

        items = db.databaseInterface().getAllItems();
        System.out.println("in displayAll item size: " + items.size() );
        // Return the raw response to the onPostExecute
        //BUT remember, items is a local Arraylist for this thread
        return items;
    }

    @Override
    protected void onPostExecute(List<RateRecord> s) {
        super.onPostExecute(s);
        adapter= new UserAdapter(items);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        MainActivity.items = items; //this modifies the items arraylist in MainActivity, very nasty way to do it
    }
}
