package com.fuzple.headup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import com.bumptech.glide.Glide;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

        final RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.digitalColckBackground);
        ImageView iv = (ImageView)findViewById(R.id.background_image);
        //GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
        GlideApp.with(this).asGif().load(R.drawable.mirei).placeholder(R.drawable.mirei).centerCrop().into(iv);
/*
        Glide.with(this).load(R.drawable.leaf).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    relativeLayout.setBackground(resource);
                }
            }
        });
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

}
