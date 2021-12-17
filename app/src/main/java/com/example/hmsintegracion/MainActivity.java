package com.example.hmsintegracion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.agconnect.remoteconfig.AGConnectConfig;
import com.huawei.agconnect.remoteconfig.ConfigValues;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.analytics.HiAnalyticsTools;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv6;
    //VARIABLES PARA PUSH KIT
    String token ="";
    private static final String TAG ="token";

    //Variable para Analytics
    HiAnalyticsInstance instance;
    Calendar c = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Analytics
        HiAnalyticsTools.enableLog();
        instance = HiAnalytics.getInstance(MainActivity.this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "sdk >= 23 M");
            if (ActivityCompat.checkSelfPermission(this,
                    ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] strings =
                        {ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, strings, 1);
            }
        }

       findViewById(R.id.button1).setOnClickListener(this);
       findViewById(R.id.button).setOnClickListener(this);
       findViewById(R.id.button6).setOnClickListener(this);
       findViewById(R.id.button7).setOnClickListener(this);
       findViewById(R.id.button8).setOnClickListener(this);
       findViewById(R.id.button9).setOnClickListener(this);
       findViewById(R.id.button4).setOnClickListener(this);

         tv6 = (TextView)findViewById(R.id.textView6);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                getToken();
                Bundle bundle = new Bundle();
                bundle.putString("fecha", Integer.toString(c.get(Calendar.DATE)) + "-" + Integer.toString(c.get(Calendar.MONTH)));
                instance.onEvent("GENERA_TOKEN", bundle);
                break;
            case R.id.button:
                Intent i1 = new Intent(MainActivity.this, LocationSiteMaps.class);
                startActivity(i1);
                break;
            case R.id.button6:
                Intent i2 = new Intent(MainActivity.this,Adskit.class);
                startActivity(i2);
                break;
            case R.id.button7:
                Intent i3 = new Intent(MainActivity.this,SafetyDetectKit.class);
                startActivity(i3);
                break;
            case R.id.button8:
                Intent i4 = new Intent(MainActivity.this,Scan.class);
                startActivity(i4);
                break;
            case R.id.button9:
                Intent i5 = new Intent(MainActivity.this,RemoteConfig.class);
                startActivity(i5);
                break;
            case R.id.button4:
                Intent i6 = new Intent(MainActivity.this,AccountKit.class);
                startActivity(i6);
                break;
            default:
                break;
        }
    }

    private void getToken() {
        // Create a thread.
        new Thread() {
            @Override
            public void run() {
                try {
                    // Obtain the app ID from the agconnect-service.json file.
                    String appId = AGConnectServicesConfig.fromContext(MainActivity.this).getString("client/app_id");

                    // Enter the token ID HCM.
                    String tokenScope = "HCM";
                    token = HmsInstanceId.getInstance(MainActivity.this).getToken(appId, tokenScope);
                    Log.i(TAG, "get token: " + token);

                    // Check whether the token is empty.
                    if(!TextUtils.isEmpty(token)) {
                        Log.i(TAG, "get token:" + token);
                        tv6.setText(token);
                  }
                } catch (ApiException e) {
                    Log.e(TAG, "get token failed, " + e);
                }
            }
        }.start();
    }
}