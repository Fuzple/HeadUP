package com.fuzple.headup;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by user on 2017-12-15.
 */

public class alarm_fragment extends android.app.Fragment{

    ListView listview ;
    Button re_btn;
    FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.alarm_fragment,container,false);
        listview = (ListView)v.findViewById(R.id.alarm_list);
        re_btn = (Button)v.findViewById(R.id.re_button);


        re_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fm = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, ((MainActivity)getActivity()).al_r_frag);
                fragmentTransaction.commit();
            }
        });

        listview.setAdapter(((MainActivity)getActivity()).adapter);
        return v;
    }
}
