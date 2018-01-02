package com.fuzple.headup;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by user on 2017-12-28.
 */

public class main extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        checkPermission();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            Toast.makeText(this,"location access",Toast.LENGTH_LONG).show();
        }

        else if(requestCode == 1)
        {
            Toast.makeText(this,"storage access",Toast.LENGTH_LONG).show();
        }



    }

    private void checkPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {

            // Should we show an explanation?
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)        ) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                dialog.setTitle("권한이 필요합니다.")
                        .setMessage("이 기능을 사용하기 위해서는 단말기의 gps 권한이 필요합니다. 계속하시겠습니까?")

                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                                }
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create()
                        .show();
            }
            // Explain to the user why we need to write the permission.
            Toast.makeText(this, "location information", Toast.LENGTH_SHORT).show();

            // MY_PERMISSION_REQUEST_STORAGE is an
            // app-defined int constant
        } else {
            // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
            Toast.makeText(this, "location information OK", Toast.LENGTH_SHORT).show();
        }

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        {

            // Should we show an explanation?
            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to write the permission.
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                dialog.setTitle("권한이 필요합니다.")

                        .setMessage("이 기능을 사용하기 위해서는 단말기의 gps 권한이 필요합니다. 계속하시겠습니까?")
                        .setCancelable(false)

                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                }
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create()
                        .show();
            }
            Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
            // MY_PERMISSION_REQUEST_STORAGE is an
            // app-defined int constant

        } else {
            // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
            Toast.makeText(this, "Read/Write external storage OK", Toast.LENGTH_SHORT).show();
        }
    }
}
