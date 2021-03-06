package dev.thrizzo.prayertimes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context, NotificationIntentService.class);
        intent1.putExtra("starting", intent.getBooleanExtra("starting", true));
        intent1.putExtra("title", intent.getStringExtra("title"));
        intent1.putExtra("body", intent.getStringExtra("body"));
        System.out.println("title = " + intent.getStringExtra("title") + " body = " + intent.getStringExtra("body"));
        context.startService(intent1);
    }
}