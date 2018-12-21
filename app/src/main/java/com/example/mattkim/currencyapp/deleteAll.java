package com.example.mattkim.currencyapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

public class deleteAll extends AsyncTask<List<RateRecord>,Void,List<RateRecord>> {
    // Variables for the search input field, and results TextViews
    private AppDatabase db;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public Context context;  //done as a hack to get app context
    List<RateRecord> items;

    // Constructor providing a reference to the views in MainActivity
    public deleteAll(AppDatabase db, RecyclerView recyclerView, RecyclerView.Adapter adapter, Context context ) {
        this.db = db;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.context = context;
    }


    @Override
    protected List<RateRecord> doInBackground(List<RateRecord>... params) {

        // Get the search string
        items = params[0];
        db.databaseInterface().dropTheTable();
        return items;
    }

    @Override
    protected void onPostExecute(List<RateRecord> s) {
        super.onPostExecute(s);
        if (items!=null) {
            System.out.println("item size: " + items.size());
            Toast.makeText(context, "Thank you for waiting!\nAll Your Data is now gone!", Toast.LENGTH_LONG).show();
            items.clear();
        }
        adapter = new UserAdapter(items);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}