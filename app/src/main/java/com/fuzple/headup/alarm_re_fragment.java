package com.fuzple.headup;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.FormatException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by user on 2017-12-15.
 */

public class alarm_re_fragment extends Fragment{

    Spinner t_sp,d_sp;
    Button set_btn,reset_btn,recan_btn,select_alarm;
    ArrayAdapter t_adapter,d_adapter;
    ArrayList<String> t_list,d_list;
    Button m_btn;
    String t,d;
    AlarmManager am;
    Calendar calendar;
    PendingIntent pender;
    Fragment frag;
    FragmentTransaction fragmentTransaction;
    FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.alarm_re_fragment,container,false);
        select_alarm = (Button)v.findViewById(R.id.select_alarm);

        frag = this;
        fm = getActivity().getFragmentManager();
        fragmentTransaction = fm.beginTransaction();

        t_sp = (Spinner)v.findViewById(R.id.t_sp);
        d_sp = (Spinner)v.findViewById(R.id.d_sp);
        m_btn = (Button)v.findViewById(R.id.m_select);

        set_btn = (Button)v.findViewById(R.id.set_btn);
        reset_btn = (Button)v.findViewById(R.id.reset_btn);
        recan_btn = (Button)v.findViewById(R.id.recan_btn);

        calendar =Calendar.getInstance();
        Intent intent = new Intent(getActivity(),AlarmReceiver.class);

        pender = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);

        select_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Integer.parseInt(t) > calendar.get(calendar.HOUR_OF_DAY) || (Integer.parseInt(t) == calendar.get(calendar.HOUR_OF_DAY) && Integer.parseInt(d) >= calendar.get(calendar.MINUTE))) {

                        ((MainActivity) getActivity()).addadapter(t, d);

                        am = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);

                        calendar.set(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DATE), Integer.parseInt(t), Integer.parseInt(d), 0);
                        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pender);

                        fragmentTransaction.replace(R.id.main_container, ((MainActivity) getActivity()).al_frag);
                        fragmentTransaction.commit();
                    } else {
                        Toast.makeText(getActivity(), "잘못된 시간", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(NumberFormatException e)
                {
                    Toast.makeText(getActivity(), "시간 선택", Toast.LENGTH_SHORT).show();
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

        t_adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, t_list);
        t_sp.setAdapter(t_adapter);
        d_adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, d_list);
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
                        Toast.makeText(getActivity(), "이미 지난 시간입니다", Toast.LENGTH_SHORT).show();
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
                    d = d_sp.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        m_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                fragmentTransaction.detach(((MainActivity) getActivity()).al_r_frag).attach(((MainActivity) getActivity()).al_r_frag).commit();
            }
        });
        recan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container, ((MainActivity) getActivity()).al_frag);
                fragmentTransaction.commit();
            }
        });

        return v;
    }
}
