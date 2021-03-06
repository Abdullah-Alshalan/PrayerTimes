package dev.thrizzo.prayertimes;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.SystemClock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;

public class SilentModeIntentService extends IntentService {

    public SilentModeIntentService() {
        super("SilentModeIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            SharedPreferences sp = getSharedPreferences("ABOODY", MODE_PRIVATE);
            if (!sp.getBoolean("silentModeEnabled", false))
                return;
            System.out.println("starting = " + intent.getBooleanExtra("starting", true));
            System.out.println("setRingerMode = " + intent.getIntExtra("setRingerMode", -1));

            boolean flag = false;
            Intent silentIntent = new Intent(this, SilentModeReceiver.class);

            if (!intent.getBooleanExtra("starting", true)) {

                AudioManager am = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);

                if (-1 != intent.getIntExtra("setRingerMode", -1)) {
                    am.setRingerMode(intent.getIntExtra("setRingerMode", -1));
                    System.out.println("setting ringer to extra");
                } else {
                    System.out.println("setting ringer to silent. was " + am.getRingerMode());
                    silentIntent.putExtra("setRingerMode", am.getRingerMode());
                    am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    flag = true;
                }

            }

            Time nextSilentModeTime = getTimeTillNextSilentMode();

            if (nextSilentModeTime != null) {
                System.out.println(nextSilentModeTime);
                silentIntent.putExtra("starting", false);

                PendingIntent pendingIntent = PendingIntent.getBroadcast
                        (getApplicationContext(), 0, silentIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
                long milis = (nextSilentModeTime.hour * 60 + nextSilentModeTime.minute + sp.getInt("silentModeDelay", 0)) * 60 * 1000;

                if (flag)
                    milis = (sp.getInt("silentModeLength", 30)) * 60 * 1000;
                milis += SystemClock.elapsedRealtime();
                System.out.println("milis = " + milis);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, milis, pendingIntent);

            }
        }
    }

    private Time getTimeTillNextSilentMode() {
        SharedPreferences sp = getSharedPreferences("ABOODY", MODE_PRIVATE);

        double latitude = sp.getFloat("lat", 0);
        double longitude = sp.getFloat("lng", 0);
        int calcMethodIndex = sp.getInt("calcMethodIndex", 0);
        int asrAdjustIndex = sp.getInt("asrAdjustIndex", 0);
        int higherLatAdjustIndex = sp.getInt("higherLatAdjustIndex", 0);

        System.out.println(calcMethodIndex + " " + asrAdjustIndex + " " + higherLatAdjustIndex);

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

        Time current = Time.parse(new SimpleDateFormat("HH:mm").format(now));
        for (int i = 0; i < prayerTimes.size(); i++) {
            Time prayer = Time.parse(prayerTimes.get(i));
            if (prayer.isBiggerThan(current)) {
                return prayer.minus(current);
            }
        }
        if (!Time.parse(prayerTimes.get(4)).isBiggerThan(current)) {
            Date tomorrow = new Date();
            tomorrow.setTime(now.getTime() + 1000 * 60 * 60 * 24);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(now);

            Time parse = Time.parse(prayers.getPrayerTimes(cal2, latitude, longitude, timezone).get(0));
            Time delta = parse.minus(current);
            delta.hour += 24;
            return delta;
        }
        return null;
    }
}