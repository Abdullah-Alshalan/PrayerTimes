package dev.thrizzo.prayertimes.activities;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import dev.thrizzo.prayertimes.NotificationReceiver;
import dev.thrizzo.prayertimes.PrayTimes;
import dev.thrizzo.prayertimes.R;
import dev.thrizzo.prayertimes.SilentModeReceiver;
import dev.thrizzo.prayertimes.Time;

public class MainActivity extends AppCompatActivity {
    public static String[] prayerNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent notifyIntent = new Intent(this, NotificationReceiver.class);
        sendBroadcast(notifyIntent);
        //        PendingIntent pendingIntent = PendingIntent.getBroadcast
//                (getApplicationContext(), 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, 1000, pendingIntent);


        Intent silentIntent = new Intent(this, SilentModeReceiver.class);
        sendBroadcast(silentIntent);
//        PendingIntent pendingIntent2 = PendingIntent.getBroadcast
//                (getApplicationContext(), 0, silentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, 1000, pendingIntent2);


        defineNotificationChannel();

        defineLanguage();

        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.mainToolBar);
        setSupportActionBar(myToolbar);

        TimeUpdateBroadcastReciver rec = new TimeUpdateBroadcastReciver();
        registerReceiver(rec, new IntentFilter("TIMEUPDATE"));

        definePrayerNames();

        final Handler handler = new Handler();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    try {
                        updateTimes();
                    } catch (Exception e) {
                    }
                });
            }
        }, 200, 1000);
    }

    private void definePrayerNames() {
        prayerNames = new String[]{
                getBaseContext().getResources().getString(R.string.fajr),
                getBaseContext().getResources().getString(R.string.dhohr),
                getBaseContext().getResources().getString(R.string.asr),
                getBaseContext().getResources().getString(R.string.maghreb),
                getBaseContext().getResources().getString(R.string.isha),
        };
    }

    private void defineLanguage() {
        SharedPreferences sp = getSharedPreferences("ABOODY", MODE_PRIVATE);
        String lang = sp.getString("lang", "ar");
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void defineNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // The id of the channel.
            String id = "my_channel_01";

            // The user-visible name of the channel.
            CharSequence name = getString(R.string.channel_prayer_name);

            // The user-visible description of the channel.
            String description = getString(R.string.channel_prayer_description);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel mChannel = new NotificationChannel(id, name, importance);

            // Configure the notification channel.
            mChannel.setDescription(description);

            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);

            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    @SuppressLint("StringFormatMatches")
    private void updateTimes() {
        SharedPreferences sp = getSharedPreferences("ABOODY", MODE_PRIVATE);
        String lang = sp.getString("lang", "ar");

        double latitude = sp.getFloat("lat", 0);
        double longitude = sp.getFloat("lng", 0);
        int calcMethodIndex = sp.getInt("calcMethodIndex", 0);
        int asrAdjustIndex = sp.getInt("asrAdjustIndex", 0);
        int higherLatAdjustIndex = sp.getInt("higherLatAdjustIndex", 0);

//        System.out.println(calcMethodIndex + " " + asrAdjustIndex + " " + higherLatAdjustIndex);

        //TODO
        double timezone = 3;
        // Test Prayer times here
        PrayTimes prayers = new PrayTimes();

        prayers.setTimeFormat(prayers.Time12);
        prayers.setCalcMethod(calcMethodIndex);
        prayers.setAsrJuristic(asrAdjustIndex);
        prayers.setAdjustHighLats(higherLatAdjustIndex);
        int[] offsets = {0, 0, 0, 0, 0, 0, 0}; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
        prayers.tune(offsets);

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        ArrayList<String> prayerTimes = prayers.getPrayerTimes(cal, latitude, longitude, timezone);

        prayerTimes.remove(1);
        prayerTimes.remove(3);

        TextView tvf = findViewById(R.id.textViewFajrTime);
        tvf.setText(convertTime(prayerTimes.get(0), lang));
        TextView tvd = findViewById(R.id.textViewDhohrTime);
        tvd.setText(convertTime(prayerTimes.get(1), lang));
        TextView tva = findViewById(R.id.textViewAsrTime);
        tva.setText(convertTime(prayerTimes.get(2), lang));
        TextView tvm = findViewById(R.id.textViewMaghrebTime);
        tvm.setText(convertTime(prayerTimes.get(3), lang));
        TextView tvi = findViewById(R.id.textViewIshaTime);
        tvi.setText(convertTime(prayerTimes.get(4), lang));

        Time current = Time.parse(new SimpleDateFormat("HH:mm").format(now));
        for (int i = 0; i < prayerTimes.size(); i++) {
            Time prayer = Time.parse(prayerTimes.get(i));
            if (prayer.isBiggerThan(current)) {
                TextView rem = findViewById(R.id.textViewTimeRemaining);
                Time delta = prayer.minus(current);
                String foramt = getBaseContext().getResources().getString(R.string.timeRemaining);
                rem.setText(String.format(foramt, prayerNames[i], delta.hour, delta.minute));
                break;
            }
        }
        if (!Time.parse(prayerTimes.get(4)).isBiggerThan(current)) {
            System.out.println("fajr");
            Date tommorow = new Date();
            tommorow.setTime(now.getTime() + 1000 * 60 * 60 * 24);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(now);

            TextView rem = findViewById(R.id.textViewTimeRemaining);
            Time delta = Time.parse(prayers.getPrayerTimes(cal2, latitude, longitude, timezone).get(0)).minus(current);
            delta.hour += 24;
            String foramt = getBaseContext().getResources().getString(R.string.timeRemaining);
            rem.setText(String.format(foramt, prayerNames[0], delta.hour, delta.minute));
        }

        TextView gregDate = findViewById(R.id.textViewGregorianDate);
        gregDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(now));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            HijrahDate hijri = HijrahChronology.INSTANCE.date(LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE)));
            TextView hijriDate = findViewById(R.id.textViewHijriDate);
            hijriDate.setText(convertTime(hijri.toString().split(" ")[2], lang));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);

        Drawable drawable = menu.findItem(R.id.action_settings).getIcon();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            System.out.println(menu.findItem(R.id.action_settings).getIconTintList());
        }
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, Color.WHITE);
        menu.findItem(R.id.action_settings).setIcon(drawable);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        } else if (itemId == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }
        return true;
    }

    private class TimeUpdateBroadcastReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("i am alive");
            updateTimes();
        }
    }

    private static final char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};

    public static String convertTime(String in, String lang) {
        if (lang.equals("en")) {
            return in;
        }
        for (int i = 0; i < arabicChars.length; i++) {
            char c = arabicChars[i];
            in = in.replaceAll(i + "", c + "");
        }
        in = in.replaceAll("am", "ص");
        in = in.replaceAll("pm", "م");
        return in;
    }
}

