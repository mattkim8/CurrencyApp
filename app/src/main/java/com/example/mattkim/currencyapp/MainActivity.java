package com.example.mattkim.currencyapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    JSONArray jsonArray;
    JSONArray jsonArray2;
    JSONObject jsonObject;
    JSONObject jsonObject2;
    public static RecyclerView recyclerView;
    ImageButton saved;
    ImageButton search;
    ImageButton convert;
    public static Context context;
    public AppDatabase db;
    public static RecyclerView.Adapter adapter;

    public static List<RateRecord> items;

    List<LiveRate> liverates = new ArrayList<>();

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ImageButton saved = (ImageButton) findViewById(R.id.savedbutton);
        ImageButton search = (ImageButton) findViewById(R.id.searchbutton);
        ImageButton convert = (ImageButton) findViewById(R.id.convertbutton);
        MainActivity.context = getApplicationContext();

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(0);

        recyclerView = (RecyclerView) findViewById(R.id.live_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
      //  Log.d(TAG, "onCreate: dsafasfasf");
     //   new DownloadJSON(progressBar).execute();
        //new DatabaseAsync(MainActivity.this).execute(null,-1,null);
        new HttpGetTask().execute("http://apilayer.net/api/live?access_key=24f6986a39c72b45c2c1801e38b93b56");
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, db.NAME).fallbackToDestructiveMigration().build();
        new displayAll(db,recyclerView,adapter,context).execute();
    }

    @Override
    protected void onStop(){
        super.onStop();
        db.close();
        //finish();
    }



    @Override
    protected void onPause(){
        super.onPause();
        db.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void searchTask(View view){

        Intent intent = new Intent(this, Search.class);
        startActivity(intent);


    }
    public void convertTask(View view){

        Intent intent = new Intent(this, Convert.class);
        startActivity(intent);


    }
    public void savedTask(View view){

        Intent intent = new Intent(this, Saved.class);
        startActivity(intent);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // --- Switch on the item ID that was clicked
        switch (item.getItemId()) {



            case R.id.menuDeleteAll:
                new deleteAll( db,  recyclerView, adapter, context).execute(items);
                break;


            case R.id.menuLoadAll:
                // --- get all data in the database
                new HttpGetTask().execute("http://apilayer.net/api/live?access_key=24f6986a39c72b45c2c1801e38b93b56");
                break;




            default:
                // --- Oops.. should never get here. A real program would
                //     throw an exception, log some data, etc
                toast("Hit Default! Should not be here!!");
                break;
        }
        return true;
    }

    public void toast(String msg) {
        // --- Show the string for a long time
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }



    private class HttpGetTask extends AsyncTask<String,Integer,String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String queryString = params[0];
            // Set up variables for the try block that need to be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String bookJSONString = null;
            try {

                URL requestURL = new URL(queryString);

                // Open the network connection.
                urlConnection = (HttpURLConnection) requestURL.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Get the InputStream.
                InputStream inputStream = urlConnection.getInputStream();

                // Read the response string into a StringBuilder.
                StringBuilder builder = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // but it does make debugging a *lot* easier if you print out the completed buffer for debugging.
                    builder.append(line + "\n");
                    publishProgress();
                }

                if (builder.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    // return null;
                    return null;
                }
                bookJSONString = builder.toString();

                // Catch errors.
            } catch (IOException e) {
                e.printStackTrace();

                // Close the connections.
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            // Return the raw response.
            return bookJSONString;
        }

        @Override
        protected void onPostExecute(String s) {
            //items = new ArrayList<Earthquakes>();
            items = new ArrayList<RateRecord>();
            double lat;
            double lon;
            double depth;
            double magnitude;
            String datetime;
            String exchange;

            try {
                // Convert the response into a JSON object.
                JSONObject jsonObject = new JSONObject(s); //get top level object
                JSONObject jsonObject2 = jsonObject.getJSONObject("quotes");
                // Get the JSONArray of book items.
               // JSONArray itemsArray = jsonObject.getJSONArray("earthquakes"); //array of earthquakes objects

                // Initialize iterator and results fields.
                //int i = 0;
                // Look for results in the items array
                for (int i = 0; i < jsonObject2.names().length(); i++) {
                 //   Log.d(TAG, "onPostExecute: hi");
                    // Get the current item information.
                    RateRecord liveRate = new RateRecord();
                    liveRate.setExchange(jsonObject2.names().getString(i));
                    liveRate.setRate(jsonObject2.get(jsonObject2.names().getString(i)).toString());
                    // Log.d(TAG, liveRate.toString());
                    items.add(liveRate);
                    items.add(liveRate);
                    //db.databaseInterface().insertAll(items.get(i)); //cant do this
                    i++;
                }
                new loadDataBase( db, recyclerView,  adapter, context).execute(items); //now enter all the data in db
            } catch (Exception e){
                // If onPostExecute does not receive a proper JSON string,
                // update the UI to show failed results.
                e.printStackTrace();
            }
        }
    }
}


    /*

    private class DownloadJSON extends AsyncTask<Void, Integer, Void> {
        ProgressBar bar;
        public  DownloadJSON(ProgressBar bar) {this.bar = bar;}


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar.setMax(100);
            bar.setProgress(0);
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Log.d(TAG, "doInBackground: dlasfdlasfjalsfdas");
            items = new ArrayList<LiveRate>();

            jsonObject = JSONfunctions.getJSONfromURL("http://apilayer.net/api/live?access_key=24f6986a39c72b45c2c1801e38b93b56");
            Log.d(TAG, "doInBackground: " + jsonObject.toString());
            try {
                jsonObject2 = jsonObject.getJSONObject("quotes");
             //   JSONArray jsonArray = jsonObject2.toJSONArray(jsonObject2.names());
              //  JSONArray jsonArray2 new JSONArray()
                //Log.d(TAG, "doInBackground: " + jsonObject2.toString());
               // Iterator x = jsonObject2.keys();
                //JSONArray jsonArray = new JSONArray();

            //    while (x.hasNext()) {
         //           String key = (String) x.next();
          //          jsonArray.put(jsonObject2.get(key));


                //}
                Log.d(TAG, "doInBackground: dfasdfasljlkjkljkljkljjlkjkljk");
             //   Log.d(TAG, "doInBackground: fda" + jsonArray.toString());
                for (int i = 0; i < jsonObject2.names().length(); i++) {
                   // Log.d(TAG, "key = " + jsonObject2.names().getString(i) + " value = " + jsonObject2.get(jsonObject2.names().getString(i)));
                    LiveRate liveRate = new LiveRate();
                    liveRate.setExchange(jsonObject2.names().getString(i));
                    liveRate.setRate(jsonObject2.get(jsonObject2.names().getString(i)).toString());
                   // Log.d(TAG, liveRate.toString());
                    items.add(liveRate);
                    publishProgress();
                    Thread.sleep(500);


                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void args){

            bar.setVisibility(View.INVISIBLE);
            mAdapter = new EventsAdapter(items,getApplicationContext());

            recyclerView.setAdapter(mAdapter);


        }
    }
}
*/

