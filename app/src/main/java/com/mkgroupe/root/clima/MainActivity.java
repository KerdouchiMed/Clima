package com.mkgroupe.root.clima;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {
    // CONSTANTS
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    // App ID to use OpenWeather data
    final String APP_ID = "e72ca729af228beabd5d20e3b7749713";

    // VIEW MEMBER VARIABLE
    TextView mLocationTV;
    TextView mTempTV;
    ImageView mWeatherSymbolIV;
    ImageButton mChangeCityIB;

    WeatherDataModel data;
    //LOCATION PROVIDER
    String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;

    //TODO DECLARATION : LOCATION MANAGER
    LocationManager mLocationManager;
    LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_controller_layout);
        mLocationTV = findViewById(R.id.locationTV);
        mTempTV = findViewById(R.id.tempTV);
        mWeatherSymbolIV = findViewById(R.id.weatherSymbolIV);
        mChangeCityIB = findViewById(R.id.changeCityButton);
        mChangeCityIB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this,ChangeCityController.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume", "onResume: ");
        Intent newCityIntent = getIntent();
        String mCityName = newCityIntent.getStringExtra("City");
        Log.d("Clima", "cityname: "+mCityName);
        if(mCityName != null && !mCityName.isEmpty())
        {
            getWeatherForNewCity(mCityName);
            Log.d("Clima", "cityname: "+mCityName);
        }
        else
        {
            getWeatherForCurrentLocation();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager !=null) mLocationManager.removeUpdates(mLocationListener);
    }

    void getWeatherForNewCity(String cityName)
    {
        RequestParams param = new RequestParams();
        param.put("q",cityName);
        param.put("appid",APP_ID);
        letDoSomeNet(param);
    }
    void getWeatherForCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Clima", "onLocationChanged: ");
                String longitude = String.valueOf(location.getLongitude());
                String latitude = String.valueOf(location.getLatitude());
                RequestParams param = new RequestParams();
                param.put("lat",latitude);
                param.put("lon",longitude);
                param.put("appid",APP_ID);
                letDoSomeNet(param);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Log.d("clima", "onStatusChanged: ");
            }

            @Override
            public void onProviderEnabled(String s) {
                Log.d("clima", "onProviderEnabled: ");
            }

            @Override
            public void onProviderDisabled(String s) {
                Log.d("clima", "onProviderDisabled: ");
            }
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},123);
            return;
        }
        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, 500, 1000, mLocationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==123)
        {
            if(grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Log.d("clima", "onRequestPermissionsResult: true");
                getWeatherForCurrentLocation();
            }
            else
            {
                Log.d("clima", "onRequestPermissionsResult: false");
            }
        }
    }

    private void letDoSomeNet(RequestParams param)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL,param,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Clima", "onSuccess: "+response.toString());
                try {
                     data = WeatherDataModel.formJSON(response);
                     updateUI(data);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(MainActivity.this,"Request Failure",Toast.LENGTH_SHORT).show();
            }
        });

    }

    void updateUI(WeatherDataModel data)
    {
        mTempTV.setText(Integer.toString(data.getTemperature())+"°C");
        mLocationTV.setText(data.getCity());
        int resID = getResources().getIdentifier(data.mIconName,"drawable",getPackageName());
        mWeatherSymbolIV.setImageResource(resID);
    }
}
