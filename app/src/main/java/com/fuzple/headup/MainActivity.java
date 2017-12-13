package com.fuzple.headup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    ListView listview ;
    Myadapter adapter;
    Button re_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alaram);

        listview = (ListView)findViewById(R.id.alram_list);
        re_btn = (Button)findViewById(R.id.re_button);

        re_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, alregister_Activity.class);
                startActivity(i);
            }
        });

        adapter = new Myadapter();
        listview.setAdapter(adapter);

        adapter.addItem("11-20-AM","monday");
    }
}
