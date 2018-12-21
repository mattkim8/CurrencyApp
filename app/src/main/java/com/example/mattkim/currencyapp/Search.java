package com.example.mattkim.currencyapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Iterator;

import static android.support.constraint.Constraints.TAG;

public class Search extends AppCompatActivity {
    JSONObject countries;
    JSONObject jobjt;
    Button getinfo;
    EditText query;
    String queryString;
    TextView currencyID;
    TextView currencyName;
    TextView currencySymbol;
    TextView countryID;
    TextView countryName;
    JSONObject jsonObject;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getinfo = (Button) findViewById(R.id.getinfo);
        query = (EditText) findViewById(R.id.query);

        currencyID = (TextView) findViewById(R.id.currencyID);
        currencyName = (TextView) findViewById(R.id.currencyName);
        currencySymbol = (TextView) findViewById(R.id.currencySymbol);
        countryID = (TextView) findViewById(R.id.countryID);
        countryName = (TextView) findViewById(R.id.countryName);

    }

    private class getCapital extends AsyncTask<String,Void,String>
    {
        private Context context;

        public getCapital(Context context){
            this.context=context;
        }
        String capital = "";
        @Override
        protected String doInBackground (String...params){
        try {
            jsonObject = new CapitalHTTPClient().getWeatherData();
            Log.d(TAG, "fdsfasf" + jsonObject.toString());
            capital = jsonObject.getString(params[0]);
            //countryID.getText().toString()
            Log.d(TAG, "getCapital: 2222222");
            Log.d(TAG, "getCapital: " + capital);
            return capital;

        } catch (Exception e) {
        }
        return capital;


    }
        @Override
        protected void onPostExecute(String capital) {
            super.onPostExecute(capital);

            Intent map = new Intent(context, MapActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("capital",capital );
            map.putExtras(bundle);
            startActivity(map);

        }
    }

    public void mapcountry(View view){

        if (countryID.getText().toString() == null){
            Toast toast = Toast.makeText(this, "No country there", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        getCapital task = new getCapital(this);
        task.execute(countryID.getText().toString());

    }

    public void searchcountries(View view) {

        if (query.getText().toString() == null){
            Toast toast = Toast.makeText(this, "No country there", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        JSONTask task = new JSONTask();
        task.execute(query.getText().toString());
    }

    private class JSONTask extends AsyncTask<String, Void, JSONObject> {



        @Override
        protected JSONObject doInBackground(String... params) {

            JSONObject jObj;
            jObj = (new WeatherHttpClient2()).getWeatherData(params[0]);

          //  JSONObject jobjt = JSONfunctions.getJSONfromURL("http://free.currencyconverterapi.com/api/v6/countries");
           // try {
            //    String queryString = params[0];
            //    countries = jobjt.getJSONObject("results");
           //     Log.d(TAG, "doInBackground: " + queryString);

             //   for (int i = 0; i < countries.names().length(); i++) {
             //       JSONObject country = countries.getJSONObject(countries.names().getString(i));
                 //   Log.d(TAG, country.getString("name"));
                 /*   Log.d(TAG, "doInBackground: 11111jj111");
                    Log.d(TAG, "doInBackground: "+country.toString());
                    Log.d(TAG, country.get("currencyId").toString());
                    */
                //    if (country.getString("name").equals(queryString) || country.getString("alpha3").equals(queryString)){

               /*         currencyName.setText("Currency Name: "+(countries.getJSONObject(countries.names().getString(i)).get("currencyName").toString()));
                        currencyID.setText("Currency ID: "+ (countries.getJSONObject(countries.names().getString(i)).get("currencyId").toString()));
                        currencyName.setText("Currency Name: "+(countries.getJSONObject(countries.names().getString(i)).get("currencyName").toString()));
                        currencySymbol.setText("CurrencySymbol: " + (countries.getJSONObject(countries.names().getString(i)).getString("currencySymbol")));
                        countryID.setText("Country ID: " + (countries.getJSONObject(countries.names().getString(i)).get("id").toString()));
                        countryName.setText("Country Name: "+(countries.getJSONObject(countries.names().getString(i)).get("name").toString()));
                 //   }
                 */

               //     liveRate.setExchange(countries.names().getString(i));
               //     liveRate.setRate(countries.get(jsonObject2.names().getString(i)).toString());
            return jObj;

        }
        @Override
        protected void onPostExecute(JSONObject jObj){
            if(jObj == null){
                Toast toast = Toast.makeText(getApplicationContext(), "Not a valid input", Toast.LENGTH_SHORT);
                toast.show();
            }


            try{

            currencyID.setText(jObj.getString("currencyId"));
            currencyName.setText(jObj.getString("currencyName"));
            currencySymbol.setText(jObj.getString("currencySymbol"));
            countryID.setText(jObj.getString("id"));
            countryName.setText(jObj.getString("name"));
        }
        catch (Exception e){}
        }

    }

    public class WeatherHttpClient2 {
        private  String BASE_URL = "http://free.currencyconverterapi.com/api/v6/countries";

        public JSONObject getWeatherData(String location) {
            HttpURLConnection con = null;
            InputStream is = null;
            JSONObject jsonObject;
            JSONObject countries;
            try {
                String city =BASE_URL;
                jsonObject = JSONfunctions.getJSONfromURL(city);
                countries = jsonObject.getJSONObject("results");
                for (int i = 0; i < countries.names().length(); i++) {
                //    Log.d(TAG, "getWeatherData: hi");
                    JSONObject country = countries.getJSONObject(countries.names().getString(i));
                    if (country.getString("name").equals(location) || country.getString("alpha3").equals(location)){
                   //     Log.d(TAG, "getWeatherData: hello");
                        return country;
                    }
                }
                return null;
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (Throwable t) {
                }
                try {
                    con.disconnect();
                } catch (Throwable t) {
                }
            }
            return null;

        }

        //public byte[] getImage(String code) {

    }

}
