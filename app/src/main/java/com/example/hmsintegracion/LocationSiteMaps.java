package com.example.hmsintegracion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.LocationServices;

import java.util.Calendar;

public class LocationSiteMaps extends AppCompatActivity {

    //VARIABLES PARA LOCATION KIT
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final String TAG ="Location";

    TextView tvl1, tvl2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_site_maps);

        tvl1=(TextView)findViewById(R.id.textView13);
        tvl2=(TextView)findViewById(R.id.textView14);

        //Creating a Location Service Client
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        findViewById(R.id.button2).setOnClickListener(this::onClick);
        findViewById(R.id.button3).setOnClickListener(this::onClick);
        findViewById(R.id.button10).setOnClickListener(this::onClick);

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button2:
                getLastLocation();
                break;
            case R.id.button3:
                Intent i = new Intent(this,MapKit.class);
                startActivity(i);
                break;
            case R.id.button10:
                Intent i2 = new Intent(this,Site.class);
                startActivity(i2);
                break;
            default:
                break;
        }
    }

    private void getLastLocation(){
        try {
            Task<Location> lastLocation = mFusedLocationProviderClient.getLastLocation();

            lastLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location == null) {
                        Log.i(TAG, "getLastLocation onSuccess location is null");
                        return;
                    }
                    Log.i(TAG,
                            "getLastLocation onSuccess location[Longitude,Latitude]:" + location.getLongitude() + ","
                                    + location.getLatitude());

                    String longitud=String.valueOf(location.getLongitude());
                    tvl1.setText(longitud);
                    String latitud=String.valueOf(location.getLatitude());
                    tvl2.setText(latitud);

                    return;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Log.e(TAG, "getLastLocation onFailure:" + e.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "getLastLocation exception:" + e.getMessage());
        }
    }
}