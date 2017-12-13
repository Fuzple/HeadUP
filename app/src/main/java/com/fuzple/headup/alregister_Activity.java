package com.fuzple.headup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by user on 2017-12-12.
 */

public class alregister_Activity extends AppCompatActivity {

    Spinner t_sp,d_sp;
    ArrayAdapter t_adap,d_adap;
    ArrayAdapter t_adapter,d_adapter;
    ArrayList<String> t_list,d_list;
    Button m_btn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alaram_re);

        t_sp = (Spinner)findViewById(R.id.t_sp);
        d_sp = (Spinner)findViewById(R.id.d_sp);
        m_btn = (Button)findViewById(R.id.m_select);

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

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        d_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

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
    }
}
