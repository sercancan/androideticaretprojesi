package com.bsmstudyo.vebateknoloji.uruntakipsistemi2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

public class YayinAliciBataryaDinle extends BroadcastReceiver {

     int bataryadurumu=0;
    @Override
    public void onReceive(Context context, Intent intent) {


       bataryadurumu  = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);

        Toast.makeText(context, "Batarya Durumu : " + bataryadurumu, Toast.LENGTH_LONG).show();

        if(bataryadurumu == 82){
            Toast.makeText(context, "KAPATILIYORR", Toast.LENGTH_LONG).show();
        }

    }


}
