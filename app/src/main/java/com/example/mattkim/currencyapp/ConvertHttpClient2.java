package com.example.mattkim.currencyapp;




import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static android.support.constraint.Constraints.TAG;


public class ConvertHttpClient2 {
    private static String BASE_URL = "https://free.currencyconverterapi.com/api/v6/convert?q=";
    private static String units = "&compact=ultra";

    public JSONObject getWeatherData(String location, String second) {
        HttpURLConnection con = null;
        InputStream is = null;
        JSONObject jsonObject;
        try {
            String city =BASE_URL + location + "_" + second + units;
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