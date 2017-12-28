package com.fuzple.headup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by user on 2017-12-12.
 */

public class alregister_Activity extends AppCompatActivity {

    Spinner t_sp,d_sp;
    Button set_btn,reset_btn,recan_btn,select_alarm;
    ArrayAdapter t_adapter,d_adapter;
    ArrayList<String> t_list,d_list;
    Button m_btn;
    String t,d;
    AlarmManager am;
    Calendar calendar;
    PendingIntent pender;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_re_fragment);

        select_alarm = (Button)findViewById(R.id.select_alarm);

        t_sp = (Spinner)findViewById(R.id.t_sp);
        d_sp = (Spinner)findViewById(R.id.d_sp);
        m_btn = (Button)findViewById(R.id.m_select);

        set_btn = (Button)findViewById(R.id.set_btn);
        reset_btn = (Button)findViewById(R.id.reset_btn);
        recan_btn = (Button)findViewById(R.id.recan_btn);
        calendar =Calendar.getInstance();
        Intent intent = new Intent(this,AlarmReceiver.class);
        pender = PendingIntent.getBroadcast(this, 0, intent, 0);
        select_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    if (Integer.parseInt(t) >= calendar.get(calendar.HOUR_OF_DAY) && Integer.parseInt(d) >= calendar.get(calendar.MINUTE)) {
                        Intent intent = new Intent();
                        intent.putExtra("t", t);
                        intent.putExtra("d", d);
                        setResult(222, intent);
                        am = (AlarmManager) getSystemService(ALARM_SERVICE);

                        calendar.set(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DATE), Integer.parseInt(t), Integer.parseInt(d), 0);
                        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pender);

                        finish();
                    } else {
                        Toast.makeText(alregister_Activity.this, "이미 지난 시간입니다", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception e)
                {
                    Toast.makeText(alregister_Activity.this, "이미 지난 시간입니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        t_list = new ArrayList<>();
        d_list = new ArrayList<>();

        t_list.add("시간 설정");
        for(int i=0;i<=23;i++)
        {
            t_list.add(Integer.toString(i));
        }

        d_list.add("분 설정");
        for(int k=0;k<=59;k++)
        {
            d_list.add(Integer.toString(k));
        }

        t_adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, t_list);
        t_sp.setAdapter(t_adapter);
        d_adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, d_list);
        d_sp.setAdapter(d_adapter);

        t_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(i != 0)
                {
                    if (Integer.parseInt(t_sp.getItemAtPosition(i).toString()) >= calendar.get(calendar.HOUR_OF_DAY)) {
                        t = t_sp.getItemAtPosition(i).toString();
                    } else {
                        Toast.makeText(alregister_Activity.this, "이미 지난 시간입니다", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        d_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(i!=0)
                {
                    if (Integer.parseInt(d_sp.getItemAtPosition(i).toString()) >= calendar.get(calendar.MINUTE)) {
                        d = d_sp.getItemAtPosition(i).toString();

                    } else {
                        Toast.makeText(alregister_Activity.this, "이미 지난 시간입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        m_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(alregister_Activity.this, MyMusicPlayer.class);
                startActivity(i);
            }
        });

        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        recan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
