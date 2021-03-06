package dev.thrizzo.prayertimes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SilentModeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context, SilentModeIntentService.class);

        System.out.println("broadcast ringerMode = " + intent.getIntExtra("setRingerMode", -1));
        intent1.putExtra("starting", intent.getBooleanExtra("starting", true));
        intent1.putExtra("setRingerMode", intent.getIntExtra("setRingerMode", -1));
        context.startService(intent1);
    }

}