package com.bsmstudyo.vebateknoloji.uruntakipsistemi2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {

    Button btn_urunler,btn_siparisler,btn_urunEkle,btn_kategoriEkle;
    Intent intent;
    YayinAliciNetDinle yayinAliciBataryaDinle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);



        //Şarj olayı
         /*   YayinAliciBataryaDinle bataryaDinle = new YayinAliciBataryaDinle();
            IntentFilter filter = new IntentFilter();
                         filter.addAction("android.intent.action.BATTERY_CHANGED");
                         filter.addAction("android.intent.action.BATTERY_LOW");
                         filter.addAction("android.intent.action.BATTERY_OKAY");

            registerReceiver(bataryaDinle,filter);
*/


       /*    yayinAliciBataryaDinle = new YayinAliciNetDinle();
            IntentFilter intentFilter = new IntentFilter();
                        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
                        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
                        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
                        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        registerReceiver(yayinAliciBataryaDinle,intentFilter);

        */
        ///CUSTOM PHONE 7.1 apı 25
        btn_urunler = (Button)findViewById(R.id.btn_urunler);
        btn_urunEkle = (Button) findViewById(R.id.btn_urunEkle);
        btn_siparisler = (Button) findViewById(R.id.btn_Siparisler);
        btn_kategoriEkle = (Button)findViewById(R.id.btn_kategoriEkle);

        btn_siparisler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 intent = new Intent(MainActivity.this,SiparislerActivity.class);
                startActivity(intent);
            }
        });
        btn_urunEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  intent = new Intent(MainActivity.this,UrunekleActivity.class);
                startActivity(intent);
            }
        });
        btn_urunler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 intent = new Intent(MainActivity.this, UrunlerActivity_Pasif.class);
                startActivity(intent);
            }
        });
        btn_kategoriEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,Kategori_Ekle_Activity.class);
                startActivity(intent);
            }
        });

    }//ONCREATE


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(yayinAliciBataryaDinle != null){
            this.unregisterReceiver(yayinAliciBataryaDinle);
        }

    }
}
