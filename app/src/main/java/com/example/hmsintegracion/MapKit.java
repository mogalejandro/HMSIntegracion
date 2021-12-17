package com.example.hmsintegracion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.MapsInitializer;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapKit extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapViewDemoActivity";
    private HuaweiMap hMap;
    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_kit);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "sdk >= 23 M");
            if (ActivityCompat.checkSelfPermission(this,
                    ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] strings = {ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                ActivityCompat.requestPermissions(this, strings, 1);
            }
        }

        mMapView = findViewById(R.id.mapView);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        MapsInitializer.setApiKey("CgB6e3x9oMXaJ6NBv8qKPv76Zu/XAcXZq8txTINh6iNQNh6jYJNmFr/6s9P/Wq0Q4tEi94qAe8kpv/YTgXQYB4HS");
        mMapView.onCreate(mapViewBundle);
        //get map instance
        mMapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(HuaweiMap huaweiMap) {
    hMap=huaweiMap;

    MarkerOptions options = new MarkerOptions().position(new LatLng(-99.35958053781573,19.36170974913152)).title("Hello Huawei Map").snippet("This is a snippet!");
    mMarker = hMap.addMarker(options);
    hMap.setMyLocationEnabled(true);
        // Enable the my-location icon.
    hMap.getUiSettings().setMyLocationButtonEnabled(true);
    addMarker(this);

    }
    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    public void addMarker(MapKit view) {
        if (null != mMarker) {
            mMarker.remove();
        }
        MarkerOptions options = new MarkerOptions().position(new LatLng(19.362002,-99.359152)).title("what you are looking for is here").snippet("Place 1");
         mMarker = hMap.addMarker(options);

    }

}