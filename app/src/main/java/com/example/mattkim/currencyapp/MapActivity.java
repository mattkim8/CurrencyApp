package com.example.mattkim.currencyapp;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
//public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap map;
    private LatLng myLocation;
    private LatLng latLng;
    private double lat1,lon1;
    TextView capitaltext;
    private String country;  //= "unknown country";
    Geocoder geocoder = null;
    String countryquery;
    ArrayList<String> locations; //will contain all the locations
    private static final String TAG = "MainActivity";
    JSONObject jsonObject;
    String capital;
    List<LatLng> ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

       // ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle("Mapper");
        //actionBar.setDisplayHomeAsUpEnabled(true);  //helps with returning to our MainActivity

        geocoder = new Geocoder(this);
        locations = new ArrayList<String>();

        // This fetches the addresses from a bundle and places them in an ArrayList
        // ArrayList will be used later by GeoCoder
        Intent arts = getIntent();
        Bundle bundle = arts.getExtras();

        String countryquery = (String) bundle.getSerializable("capital");
        Log.d(TAG, "onCreate: 111111111");
        Log.d(TAG, "onCreate: "+ countryquery);

        capitaltext = (TextView)findViewById(R.id.capital) ;
        capitaltext.setText(countryquery);

      /*  eq = (EartquakeRecord) bundle.getSerializable("earthquake");

        lat1 = eq.getLat();
        lon1 = eq.getLon();
        */

        //String msg = "Lng: " + eq.getLon()+ " Lat: " + eq.getLat() + " name: " + eq.getName();

        //gets the maps to load
        MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.the_map);
        mf.getMapAsync(this);

        //Log.d(TAG, "onCreate: "+countryquery);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //this will catch the <- arrow
        //and return to MainActivity
        //needed since we use fragments to map sites
        switch (item.getItemId()) {
            case android.R.id.home:
                System.out.println("1. here here in go back");
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.out.println("2. here here in go back");
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onMapReady(GoogleMap map) {    // map is loaded but not laid out yet
        this.map = map;
        map.setOnMapLoadedCallback(this);      // calls onMapLoaded when layout done
        UiSettings mapSettings;
        mapSettings = map.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);
    }

    // maps are loaded and this is where I should perform the getMoreInfo() to grab more data
    //note use of geocoder.getFromLocationName() to find LonLat from address
    @Override
    public void onMapLoaded() {
        // code to run when the map has loaded
       // Log.d(TAG, "onMapLoaded: "+countryquery);
        getMoreInfo(); // call this --> use a geoCoder to find the location of the eq
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getApplicationContext(), "Info window clicked", Toast.LENGTH_LONG).show();
                String url = "http://en.wikipedia.org/wiki/";
                String query = capitaltext.getText().toString();
                String final_url = url + query;
                Log.d(TAG, "onInfoWindowClick: " + url);
                Uri uri = Uri.parse(final_url);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
    }

    public void getMoreInfo() {

     //   System.out.println("in getMoreInfo " + lat1 + " " + lon1);
    //    latLng = new LatLng(lat1, lon1);  //used in addMarker below for placing a marker at the Longitude/Latitude spot
        String s = capitaltext.getText().toString();
        Geocoder gcd = new Geocoder(this);
        List<LatLng> ll = new ArrayList<LatLng>();
        Log.d(TAG, "getMoreInfo: "+s);
        try {
            List<Address> list = gcd.getFromLocationName(s,1);
            if (list != null & list.size() > 0) {
                country = list.get(0).getCountryName(); //grab country name from GeoCoder data from Google
                if (country==null)
                    country = "unknown country";
                System.out.println("in map getMoreInfo country " + country);
                for(Address a : list){
                    if(a.hasLatitude() && a.hasLongitude()){
                        ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                    }
                }
                latLng = ll.get(0);


            }
            else { //no location found
                country = "unknown country";
                System.out.println("in getMoreInfo no location found");
            }
        } catch (IOException e) //no geo address found
        {
            country = "unknown country";
            Log.v("in map new test","hhhh");
        }
        catch(Exception e){}
        Log.d(TAG, "getMoreInfo: 111111" + ll.toString());
        // puts marker icon at location
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Name: " + String.valueOf(s))
                .snippet("Country: " + country)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14), 3000, null);
    }
}
