package com.example.mattkim.currencyapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Saved extends AppCompatActivity implements RateAdapter.ClickListener{

    private TextView rate;
    private EditText base;
    private EditText second;
    private Button convert;
    private Button save;
    public static String finalcurrency;
    //public EventDatabase mb;
    public String exchange;
    public String rater;
    List<RateRecord2> events = new ArrayList<>();
    RateAdapter adapter;

    private static final String TAG = "MainActivity";
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: hii");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        Log.d(TAG, "onCreate: 22222");
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Log.d(TAG, "onCreate: 1111");
        new DatabaseAsync().execute(null,-1,null);



    }

    @Override
    public void onClick(View view, int position) {
        //USE THIS ONLY IF NOT USING RecyclerTouchListener
        // Toast toast = Toast.makeText(this, "You just clicked on:" + events.get(position).toString(), Toast.LENGTH_SHORT);
        // toast.show();

    }


    @Override
    public void onLongClick(View view, int position) {
        //show Alertdialog to edit or update the
        //USE THIS ONLY IF NOT USING RecyclerTouchListener
        showActionsDialog(position);
    }

    public void showActionsDialog(final int position){
        new DatabaseAsync().execute(null,position,null);
    }

    private class DatabaseAsync extends AsyncTask<Object, Void, List<RateRecord2>> {
        public RateAdapter.ClickListener c;  //need to get main



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: #2222222");
        }

        @Override
        protected List<RateRecord2> doInBackground(Object... params) {

           /* Log.d(TAG, "doInBackground: #3333333");

            String exchage = (String) params[0];
            String rater =  (String) params[1];

            RateRecord2 event = new RateRecord2();
            event.setExchange(exchange);
            event.setRate(rater);
            */
            //EventDatabase.getEventDatabase(getApplicationContext()).eventDao().addEvent(event);


            /*
            Boolean shouldUpdate = (Boolean) params[0];
            int position = (int) params[1];
            Log.d(TAG, "doInBackground: hi");
            String title2 = (String) params[2];
            Log.d(TAG, "doInBackground: hi2"+title2);
            String detail = (String) params[3];
            String author = (String) params[4];
            Log.d(TAG, "doInBackground: hi5");
            String publisher = (String) params[5];
            Log.d(TAG, "doInBackground: 77777777");
            */

            //check whether to add add or update event based on if shouldUpdate is null//so no update since shouldUpdate == null
            Log.d(TAG, "doInBackground: 3232132144444444321");
            //delete all if postion is = -2, really bad, i should fix this
            // if (position == -2)
            //delete all events  from database
            //EventDatabase.getEventDatabase(getApplicationContext()).eventDao().dropTheTable();

            //delete event
                /*else if (position != -1) { //-1 means delete a specific event
                    Event event = events.get(position);

                    //delete event from database
                    EventDatabase.getEventDatabase(getApplicationContext()).eventDao().deleteEvent(event);
                }*/


            Log.d(TAG, "doInBackground: #444444");

            //get events from database, also not a great way to do this
            List<RateRecord2> events = EventDatabase.getEventDatabase(getApplicationContext()).eventDao().getEvents();
            return events;

        }

        @Override
        protected void onPostExecute(List<RateRecord2> items) {

            //get list of events from doInBackground()
            events = items;

            adapter = new RateAdapter(events,getApplicationContext());
            Log.d(TAG, "onPostExecute: " + events.toString());


            adapter.setClickListener(c);
            recyclerView.setAdapter(adapter);




        }
    }








}
