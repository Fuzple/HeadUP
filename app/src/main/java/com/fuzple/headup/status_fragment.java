package com.fuzple.headup;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by user on 2017-12-21.
 */

public class status_fragment extends Fragment {

    BluetoothAdapter mbta;
    ImageView iv;
    LinearLayout ll;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {

        View v =inflater.inflate(R.layout.status,container,false);
        iv = new ImageView(getActivity());
        ll = (LinearLayout)v.findViewById(R.id.status_linear);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);

        mbta = BluetoothAdapter.getDefaultAdapter();

        if(mbta.isEnabled())
        {
            iv.setLayoutParams(lp);
            ll.addView(iv);
        }
        else
        {
            ll.removeView(iv);
        }
        return v;
    }
}
