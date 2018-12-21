package com.example.mattkim.currencyapp;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Convert extends AppCompatActivity {

    private TextView rate;
    private EditText base;
    private EditText second;
    private Button convert;
    private Button save;
    public static String finalcurrency;
    public AppDatabase mb;
    public String exchange;
    public String rater;
    public String first;
    public String next;
    public String third;
    List<RateRecord2> events = new ArrayList<>();

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        rate = (TextView) findViewById(R.id.finalMoney);
        base = (EditText) findViewById(R.id.Currency1);
        second = (EditText) findViewById(R.id.Currency2);
        convert = (Button) findViewById(R.id.convertbutton);
        save = (Button) findViewById(R.id.savebutton);

        mb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, mb.NAME).fallbackToDestructiveMigration().build();

    }

    public void startTask(View view){

        JSONTask task = new JSONTask();
        task.execute(base.getText().toString(),second.getText().toString());

    }

    public void switchTask(View view){

        String first = base.getText().toString();
        String last = second.getText().toString();

        base.setText(last);
        second.setText(first);

        JSONTask task = new JSONTask();
        task.execute(last, first);


    }


    private class JSONTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            JSONObject jObj;
            jObj = ((new ConvertHttpClient2()).getWeatherData(params[0],params[1]));
            try {
                //call JSON parser to extract data from JSONobject into weather object
                String finalcurrency = jObj.getString(params[0]+"_"+params[1]);
                return finalcurrency;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return finalcurrency;

        }

        @Override
        protected void onPostExecute(String finalcurrency) {
            super.onPostExecute(finalcurrency);

            if (finalcurrency == null){
                Toast toast = Toast.makeText(getApplicationContext(), "Enter valid currencies", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }


            rate.setText(""+finalcurrency);

        }
    }



    public void saveTask(View view){


        if(rate.getText().toString() == null ||base.getText().toString() == null ){
            Toast toast = Toast.makeText(getApplicationContext(), "Enter valid currencies", Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "Rate Saved!", Toast.LENGTH_SHORT);
            toast.show();
            new DatabaseAsync().execute(base.getText().toString(),second.getText().toString(),rate.getText().toString());
        }
    }



    private class DatabaseAsync extends AsyncTask<Object, Void, List<RateRecord2>>{
        public EventsAdapter.ClickListener c;  //need to get main


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: #2222222");
        }

        @Override
        protected List<RateRecord2> doInBackground(Object... params) {

            Log.d(TAG, "doInBackground: #3333333");

            String first = (String) params[0];
            String next =  (String) params[1];
            String third = (String) params[2];

            RateRecord2 event = new RateRecord2();
            event.setExchange(first + " - "+ next);
            event.setRate(third);
            EventDatabase.getEventDatabase(getApplicationContext()).eventDao().addEvent(event);


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
          //  List<RateRecord> events = EventDatabase.getEventDatabase(getApplicationContext()).eventDao().getEvents();
            return events;

        }

        @Override
        protected void onPostExecute(List<RateRecord2> items) {

            //get list of events from doInBackground()
            events = items;


        }
    }




}
