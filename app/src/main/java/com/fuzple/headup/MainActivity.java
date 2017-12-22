package com.fuzple.headup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    status_fragment st_frag;
    alarm_fragment al_frag;
    alarm_re_fragment al_r_frag;
    static Myadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);

        st_frag = new status_fragment();
        al_frag = new alarm_fragment();
        al_r_frag = new alarm_re_fragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction tranc = fm.beginTransaction();

        adapter = new Myadapter();
        tranc.replace(R.id.status_container,st_frag);
        tranc.replace(R.id.main_container,al_frag);
        tranc.commit();
    }

    public void addadapter(String t,String d)
    {
        adapter.addItem(t,d);
        adapter.notifyDataSetChanged();
    }

}
