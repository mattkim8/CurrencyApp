package com.example.mattkim.currencyapp;




import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;

import static android.support.constraint.Constraints.TAG;


public class CapitalHTTPClient {
    private static String BASE_URL = "http://country.io/capital.json";

    public JSONObject getWeatherData() {
        HttpURLConnection con = null;
        InputStream is = null;
        JSONObject jsonObject;
        try {
            String city =BASE_URL;
            jsonObject = JSONfunctions.getJSONfromURL(city);
            Log.d(TAG, "getWeatherData: " +jsonObject.toString());
            return jsonObject;
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