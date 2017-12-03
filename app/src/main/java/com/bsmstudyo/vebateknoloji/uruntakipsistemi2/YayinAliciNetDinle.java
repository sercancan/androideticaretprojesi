package com.bsmstudyo.vebateknoloji.uruntakipsistemi2;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.widget.Toast;


public class YayinAliciNetDinle extends BroadcastReceiver {

    WifiManager wifiManager;
    ConnectivityManager connectivityManager;

    @Override
    public void onReceive(final Context context, Intent intent) {

        final String aaction = intent.getAction();
        wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(aaction)){

            //Toast.makeText(context,"---"+aaction,Toast.LENGTH_LONG).show();

            final int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);

            if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
              //  Toast.makeText(context,"ACIKLOO VIWI",Toast.LENGTH_LONG).show();
            }else if(state == WifiP2pManager.WIFI_P2P_STATE_DISABLED){
                
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Internet Bağlı Değil");
                builder.setMessage("Internete Bağlanmadan işlem yapamazsınız.Bağlanmak istediğiniz türü seçiniz.");
                builder.setPositiveButton("WIFI AÇ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wifiManager.setWifiEnabled(true);

                           /* if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                                Toast.makeText(context,"ACILDI",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(context,"ACILMADI",Toast.LENGTH_LONG).show();
                                wifiManager.setWifiEnabled(false);
                            }
                            */
                    }
                });
                builder.setNeutralButton("MOBILI AÇ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("GERI DÖN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }else{
           // Toast.makeText(context,"else "+aaction,Toast.LENGTH_LONG).show();
        }

       /*YenerCevik yenerCevik = new YenerCevik();
                   yenerCevik.execute(context);*/

    }

    public class YenerCevik extends AsyncTask<Context,Void,Integer>{
        private int sonuc;
        Context context = null;
        WifiManager  wifiManageri = null;

        @Override
        protected Integer doInBackground(Context... params) {
            context = params[0];
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


            wifiManageri = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            if (wifiManager.isWifiEnabled() == false) {
                sonuc = 1;
                return sonuc;
            } else if (!mobile.isConnected()) {
                sonuc = 2;
                return sonuc;

            }
                return null;
        }//FUNC->

        @Override
        protected void onPostExecute(Integer aVoid) {

            Toast.makeText(context,"---"+aVoid,Toast.LENGTH_LONG).show();
            if(aVoid == 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("WIFI Durumu");
                builder.setMessage("WIFI ile bağlanmak istermisiniz");
                builder.setPositiveButton("WIFI AÇ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wifiManager.setWifiEnabled(true);
                    }
                });
                builder.setNegativeButton("GERI DÖN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  wifiManager.setWifiEnabled(false);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }
    }//ASYNCTSK CLASS

}
