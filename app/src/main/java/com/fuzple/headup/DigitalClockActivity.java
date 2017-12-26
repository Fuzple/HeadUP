package com.fuzple.headup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;


public class DigitalClockActivity extends AppCompatActivity {

    boolean mLocationPermissionGranted;
    FusedLocationProviderClient mFusedLocationClient;

    Typeface weatherFont;

    TextView currentTemperatureField;
    TextView weatherIcon;

    Handler handler;

    public DigitalClockActivity() {
        handler = new Handler();
    }

    TextView textDay; //요일 표시용
    TextView wi, gi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //몰입모드---
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("Is on?", "Turning immersive mode mode off. ");
        } else {
            Log.i("Is on?", "Turning immersive mode mode on.");
        }
        // 몰입 모드를 꼭 적용해야 한다면 아래의 3가지 속성을 모두 적용시켜야 합니다
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //----몰입모드

        setContentView(R.layout.activity_digital_clock);

        weatherFont = Typeface.createFromAsset(this.getAssets(), "fonts/weather.ttf"); //날씨 아이콘 폰트
        weatherIcon = (TextView)findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            wi = (TextView)findViewById(R.id.textWi);
                            gi = (TextView)findViewById(R.id.textGyo);
                            wi.setText("위도 : " + location.getLatitude());
                            gi.setText("경도 : " + location.getLongitude());
                            updateWeatherData(location.getLatitude(), location.getLongitude());
                        }
                    }
                });
        /*
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try{
            // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
            //lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
        }catch(SecurityException ex){
        }
        */


        try {
            dateDayColor();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("MissingPermission")
    private void getLocationPermission(){
        if(PermissionUtil.checkPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, PermissionUtil.PERMISSIONS_LOCATION, PermissionUtil.REQUEST_LOCATION);
        }
    }


    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);
            double longitude = location.getLongitude(); //경도
            double latitude = location.getLatitude();   //위도
            double altitude = location.getAltitude();   //고도
            float accuracy = location.getAccuracy();    //정확도
            String provider = location.getProvider();   //위치제공자
            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
            wi.setText("위도 : " + longitude);
            gi.setText("경도 : " + latitude);
        }
        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };

    public void updateWeatherData(final double lat, final  double lon){
        new Thread(){
            public void run(){
                final JSONObject json = RemoteFetch.getJSON(lat, lon);
                if(json == null){

                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json){
        try {
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");

            currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
            currentTemperatureField.setText(
                    String.format("%.2f", main.getDouble("temp"))+ " ℃");

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        weatherIcon = (TextView)findViewById(R.id.weather_icon);
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = this.getString(R.string.weather_sunny);
            } else {
                icon = this.getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = this.getString(R.string.weather_thunder);
                    break;
                case 3 : icon = this.getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = this.getString(R.string.weather_foggy);
                    break;
                case 8 : icon = this.getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = this.getString(R.string.weather_snowy);
                    break;
                case 5 : icon = this.getString(R.string.weather_rainy);
                    break;
            }
        }
        weatherIcon.setText(icon);
    }

    //해당 날짜 색상 변경
    public void dateDayColor() throws Exception {

        Calendar cal = Calendar.getInstance();
        int dayNum = cal.get(Calendar.DAY_OF_WEEK);

        switch (dayNum) {
            case 1:
                textDay = (TextView)findViewById(R.id.textSun);
                textDay.setTextColor(Color.parseColor("#ff0000"));
                break;
            case 2:
                textDay = (TextView)findViewById(R.id.textMon);
                textDay.setTextColor(Color.parseColor("#ff0000"));
                break;
            case 3:
                textDay = (TextView)findViewById(R.id.textTue);
                textDay.setTextColor(Color.parseColor("#ff0000"));
                break;
            case 4:
                textDay = (TextView)findViewById(R.id.textWed);
                textDay.setTextColor(Color.parseColor("#ff0000"));
                break;
            case 5:
                textDay = (TextView)findViewById(R.id.textThur);
                textDay.setTextColor(Color.parseColor("#ff0000"));
                break;
            case 6:
                textDay = (TextView)findViewById(R.id.textFri);
                textDay.setTextColor(Color.parseColor("#ff0000"));
                break;
            case 7:
                textDay = (TextView)findViewById(R.id.textSat);
                textDay.setTextColor(Color.parseColor("#ff0000"));
                break;

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    wi = (TextView)findViewById(R.id.textWi);
                    gi = (TextView)findViewById(R.id.textGyo);
                    final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    try{
                        // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                                100, // 통지사이의 최소 시간간격 (miliSecond)
                                1, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                                100, // 통지사이의 최소 시간간격 (miliSecond)
                                1, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);
                        //lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
                    }catch(SecurityException ex){
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
