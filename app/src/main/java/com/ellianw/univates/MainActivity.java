package com.ellianw.univates;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener, SensorEventListener {
    private TextView tvFirPos = null;
    private TextView tvActPos = null;
    private TextView tvDistance = null;
    private Button btnFirstPos = null;
    private Button btnCalculate = null;

    private LocationManager locationManager = null;
    private SensorManager sensorManager = null;

    private Location firstLocation = null;
    private Location actualLocation = null;

    private String angle = null;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar appBar = findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        tvFirPos = findViewById(R.id.firstPosition);
        btnFirstPos = findViewById(R.id.initialPosBtn);
        tvActPos = findViewById(R.id.actualPosition);
        btnCalculate = findViewById(R.id.calculatePosBtn);
        tvDistance = findViewById(R.id.distance);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            sensorManager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        btnFirstPos.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                firstLocation = getLastKnownLocation();
                tvFirPos.setText("Precisão:" + firstLocation.getAccuracy()
                        + "\nAltitude:" + firstLocation.getAltitude()
                        + "\nLatitude:" + firstLocation.getLatitude()
                        + "\nLongitude:" + firstLocation.getLongitude());
            }
        });
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                float[] distance = getDistanceBetweenTwoLocs(firstLocation,actualLocation);
                tvDistance.setText("Distancia entre posições: "+ Math.round(distance[0])+" metros"
                        +"\nÂngulo entre posições: "+Math.round(distance[1]));
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateActualPos(){
        tvActPos.setText("Latitude: "+actualLocation.getLatitude()
                +"\nLongitude: "+actualLocation.getLongitude()
                +"\nAltitude: "+actualLocation.getAltitude()
                +"\nAngulo: "+angle);
    }

    private Location getLastKnownLocation() {
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private float[] getDistanceBetweenTwoLocs(Location start, Location end) {
        float[] distance = new float[2];

        Location.distanceBetween(start.getLatitude(),start.getLongitude(),
                end.getLatitude(),end.getLongitude(),distance);

        return distance;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        actualLocation = location;
        updateActualPos();
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            angle = "X: " + event.values[0] + " Y: " + event.values[1] + " Z: " + event.values[2];
            if (actualLocation != null) {
                updateActualPos();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
