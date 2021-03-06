package dev.thrizzo.prayertimes;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.core.app.NotificationManagerCompat;
import dev.thrizzo.prayertimes.activities.MainActivity;

public class NotificationIntentService extends IntentService {
    private static final int NOTIFICATION_ID = 3;

    public NotificationIntentService() {
        super("NotificationIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences sp = getSharedPreferences("ABOODY", MODE_PRIVATE);

        if (!sp.getBoolean("notificationEnabled", true)) {
            return;
        }
        //TODO enable/disable before after
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            System.out.println("starting = " + intent.getBooleanExtra("starting", true));
            if (!intent.getBooleanExtra("starting", true)) {
                System.out.println("should send notification now");
                Notification.Builder builder = new Notification.Builder(this)
                        .setContentTitle(intent.getStringExtra("title"))
                        .setContentText(intent.getStringExtra("body"))
                        .setSmallIcon(R.drawable.notification_icon)
                        .setChannelId("my_channel_01");
                Intent notifyIntent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                //to be able to launch your activity from the notification
                builder.setContentIntent(pendingIntent);
                Notification notificationCompat = builder.build();
                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
                managerCompat.notify(NOTIFICATION_ID, notificationCompat);
                System.out.println("notification sent");
            }

            System.out.println("setting next notification");
            Object[] nextNotificationTimeRes = getTimeTillNextNotification();
            Time nextNotificationTime = (Time) nextNotificationTimeRes[0];
            if (nextNotificationTime != null) {
                System.out.println(nextNotificationTime);
                Intent notifyIntent = new Intent(this, NotificationReceiver.class);
                notifyIntent.putExtra("starting", false);
                notifyIntent.putExtra("title", nextNotificationTimeRes[1].toString());
                notifyIntent.putExtra("body", nextNotificationTimeRes[2].toString());

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
                long milis = (nextNotificationTime.hour * 60 + nextNotificationTime.minute) * 60 * 1000;
                milis += SystemClock.elapsedRealtime();
                System.out.println("milis = " + milis);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, milis, pendingIntent);
            }
        }
    }

    private Object[] getTimeTillNextNotification() {
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

        Time isha = Time.parse(prayerTimes.get(4));
        prayerTimes.add(isha.plus(new Time(0, sp.getInt("notificationIshaAfter", 15))).toString());
        prayerTimes.add(4, isha.minus(new Time(0, sp.getInt("notificationIshaBefore", 5))).toString());


        Time maghreb = Time.parse(prayerTimes.get(3));
        prayerTimes.add(4, maghreb.plus(new Time(0, sp.getInt("notificationMaghrebAfter", 15))).toString());
        prayerTimes.add(3, maghreb.minus(new Time(0, sp.getInt("notificationMaghrebBefore", 5))).toString());

        Time asr = Time.parse(prayerTimes.get(2));
        prayerTimes.add(3, asr.plus(new Time(0, sp.getInt("notificationAsrAfter", 15))).toString());
        prayerTimes.add(2, asr.minus(new Time(0, sp.getInt("notificationAsrBefore", 5))).toString());

        Time dhohr = Time.parse(prayerTimes.get(1));
        prayerTimes.add(2, dhohr.plus(new Time(0, sp.getInt("notificationDhohrAfter", 15))).toString());
        prayerTimes.add(1, dhohr.minus(new Time(0, sp.getInt("notificationDhohrBefore", 5))).toString());


        Time fajr = Time.parse(prayerTimes.get(0));
        prayerTimes.add(1, fajr.plus(new Time(0, sp.getInt("notificationFajrAfter", 15))).toString());
        prayerTimes.add(0, fajr.minus(new Time(0, sp.getInt("notificationFajrBefore", 5))).toString());

        System.out.println(prayerTimes);


        Time current = Time.parse(new SimpleDateFormat("HH:mm").format(now));
        for (int i = 0; i < prayerTimes.size(); i++) {
            Time prayer = Time.parse(prayerTimes.get(i));
            if (prayer.isBiggerThan(current)) {
                return new Object[]{prayer.minus(current), MainActivity.prayerNames[i / 3], i % 3 == 0 ? getBaseContext().getResources().getString(R.string.beforeAthan)
                        : (i % 3 == 1 ? getBaseContext().getResources().getString(R.string.athan) : getBaseContext().getResources().getString(R.string.iqama))};
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
            return new Object[]{delta, MainActivity.prayerNames[0], "Before"};
        }
        return null;
    }

}