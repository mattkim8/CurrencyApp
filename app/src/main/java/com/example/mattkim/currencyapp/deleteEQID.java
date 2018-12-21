package com.example.mattkim.currencyapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

public class deleteEQID extends AsyncTask<String,Void,List<RateRecord>> {
    // Variables for the search input field, and results TextViews
    private AppDatabase db;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public Context context;  //done as a hack to get app context
    String eqid;
    String exchange;
    List<RateRecord> items;
    int index;
    // Constructor providing a reference to the views in MainActivity
    public deleteEQID(AppDatabase db, RecyclerView recyclerView, RecyclerView.Adapter adapter, Context context , List<RateRecord> items, int index) {
        this.db = db;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.context = context;
        this.items = items;
        this.index = index;
    }

    @Override
    protected List<RateRecord> doInBackground(String... params) {
        exchange  = params[0];
        db.databaseInterface().deleteItems(exchange); //this removes from DB, must be thread based on eqid
        return items;
    }

    @Override
    protected void onPostExecute(List<RateRecord> s) {
        super.onPostExecute(s);
        items.remove(items.get(index));
        adapter= new UserAdapter(items);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        Toast toast = Toast.makeText(context, "Deleting Rate:" + items.get(index).getExchange(), Toast.LENGTH_SHORT);
        toast.show();
    }
}