package com.fuzple.headup;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class DigitalClockActivity extends AppCompatActivity {

    Typeface weatherFont;

    TextView currentTemperatureField;
    TextView weatherIcon;

    Handler handler;

    public DigitalClockActivity(){
        handler = new Handler();
    }

    TextView textDay; //요일 표시용


    @SuppressLint("MissingPermission")

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

        weatherFont = Typeface.createFromAsset(this.getAssets(), "fonts/weather.ttf");
        weatherIcon = (TextView)findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);
        //weatherIcon.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts//weather.ttf"));
        updateWeatherData("Busan");

        try {
            dateDayColor();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = RemoteFetch.getJSON(city);
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
