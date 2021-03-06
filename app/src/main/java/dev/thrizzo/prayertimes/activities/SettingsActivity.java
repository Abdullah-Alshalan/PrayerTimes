package dev.thrizzo.prayertimes.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.IntentCompat;
import dev.thrizzo.prayertimes.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_settings);

        findViewById(R.id.linearLayoutSilentMode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, SilentModeActivity.class));
            }
        });
        findViewById(R.id.linearLayoutNotifications).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, NotificationsActivity.class));
            }
        });
        findViewById(R.id.linearLayoutCalculationMethod).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, CalculationMethodActivity.class));
            }
        });


        SharedPreferences mySharedPreferences = getSharedPreferences("ABOODY", MODE_PRIVATE);

        findViewById(R.id.linearLayoutLanguage).setOnClickListener(view -> {

            String prevLang = mySharedPreferences.getString("lang", "en");
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("lang", prevLang.equals("en") ? "ar" : "en");
            editor.commit();

//            Intent mStartActivity = new Intent(getBaseContext(), MainActivity.class);
//            int mPendingIntentId = 123456;
//            PendingIntent mPendingIntent = PendingIntent.getActivity(getBaseContext(), mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
//            AlarmManager mgr = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
//            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
//            System.exit(0);
            Intent mainIntent = IntentCompat.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_LAUNCHER);
            getApplicationContext().startActivity(mainIntent);
            this.finishAffinity();
        });


        Toolbar myToolbar = findViewById(R.id.mainToolBar);
        setSupportActionBar(myToolbar);

        TextView locationD = findViewById(R.id.textViewLocationD);
        locationD.setText(String.format(getBaseContext().getResources().getString(R.string.locationD), mySharedPreferences.getString("city", null) == null ? "Outside city borders" : mySharedPreferences.getString("city", "")));


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final MyLocationListener locationListener = new MyLocationListener();

        checkPermission();
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

        findViewById(R.id.linearLayoutLocation).setOnClickListener(view -> {
            System.out.println("hello location");
            System.out.println("lat " + locationListener.lat
                    + " lng " + locationListener.lng + " city " + locationListener.city);

            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putFloat("lat", (float) locationListener.lat);
            editor.putFloat("lng", (float) locationListener.lng);
            editor.putString("city", locationListener.city);
            editor.commit();

            sendBroadcast(new Intent("TIMEUPDATE"));
            locationD.setText(String.format(getBaseContext().getResources().getString(R.string.locationD), locationListener.city == null ? "Outside city borders" : locationListener.city));
        });


    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,}, 1);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

        } else {
            checkPermission();
        }
    }

    private class MyLocationListener implements LocationListener {

        public double lat;
        public double lng;
        public String city;

        @Override
        public void onLocationChanged(Location loc) {
            lng = loc.getLongitude();
            lat = loc.getLatitude();

            /*------- To get city name from coordinates -------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.ENGLISH);
            System.out.println("testing ======================================");
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    cityName = addresses.get(0).getLocality();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            city = cityName;
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}
