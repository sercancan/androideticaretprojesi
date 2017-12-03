package com.bsmstudyo.vebateknoloji.uruntakipsistemi2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UrunlerActivity_Aktif extends AppCompatActivity {


   private RecyclerView recyclerView;
   private RecyclerView.Adapter adapter;
   List<ProductClass>  productClassList;

    private String URL = "SHOW PRODUCTS AKTIF SERVISI" ;
    private String RESIM_URL = "RESIMLER /KLASORU";

    Toolbar toolbar_urunler;
    private int menu_liste_secimi = 1;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor mPrefsEditor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                //BILDIRIM CUBUGUUNU GIZLIYORUOM
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_urunler_aktif);

       /*
        sharedPreferences = getSharedPreferences("xmlFile",MODE_PRIVATE);
        mPrefsEditor = sharedPreferences.edit();
        menu_liste_secimi =sharedPreferences.getInt("secim",3);
        */

        //BURDADA actionbarı gizliyorum aşagıdaki fonksiyon "AppCompatActivity" Sınıfına ozel bir fonksiyon
        //getSupportActionBar().hide();

        ActionBar actionBar = getSupportActionBar();
                  actionBar.setTitle("Ürünler");
                  actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ActionBarRengi)));
                  actionBar.setDisplayHomeAsUpEnabled(false);// TITLE ISMININ YANINA GERI BUTONU KOYAR FALSE YAPTIK GIZLEDIK


        //toolbar_urunler = (Toolbar)findViewById(R.id.toolbar_urunler);
        ///setSupportActionBar(toolbar_urunler);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_urunler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productClassList = new ArrayList<ProductClass>();
           // productClassList.clear();
                parseUrunlerGel(new CallBack() {
                    @Override
                    public void onSuccess(List<ProductClass> productClassList) {
                        adapter = new Urunler_Adapter(productClassList,UrunlerActivity_Aktif.this);
                        recyclerView.setAdapter(adapter);
                    }
                    @Override
                    public void onFail(String msg) {

                    }
                });

    }//oncreate FUNC


    public void parseUrunlerGel(final CallBack onCallBack){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                int count = 0;
                while(count < response.length()){
                    try{
                        JSONObject obj = response.getJSONObject(count);
                        ProductClass prd = new ProductClass();
                        prd.setUrunKodu(obj.getString("model"));
                        prd.setUrunAdi(obj.getString("name"));
                        prd.setResimYolu(obj.getString("image"));
                        prd.setFiyat(obj.getInt("fiyat"));
                        prd.setTarih(obj.getString("tarih"));
                        prd.setYayinDurumu(obj.getInt("yayin"));
                        prd.setKategori_id(obj.getInt("kategori"));
                        prd.setNumara(obj.getInt("product_id"));
                        prd.setMenu_liste_secimi(menu_liste_secimi);
                        productClassList.add(prd);
                    }catch (JSONException er){
                        Toast.makeText(getApplicationContext(),"JSONARRAYREQUEST HATA",Toast.LENGTH_LONG).show();
                    }
                    count++;
                }//WHILE
                onCallBack.onSuccess(productClassList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error instanceof NoConnectionError){
                    Toast.makeText(getApplicationContext(), "Internet erişimi bulunamadı..", Toast.LENGTH_SHORT).show();
                    UrunlerActivity_Aktif.this.finish();
                }else if(error instanceof TimeoutError){
                    Toast.makeText(getApplicationContext(), "Zaman Aşımı Hatası", Toast.LENGTH_SHORT).show();
                }else if(error instanceof ParseError){
                    Toast.makeText(getApplicationContext(), "Veriler Gelirken bir Hata oluştu.", Toast.LENGTH_SHORT).show();
                }else if(error instanceof NetworkError){
                    Toast.makeText(getApplicationContext(), "Network Hatası.", Toast.LENGTH_SHORT).show();
                }else if(error instanceof ServerError){
                    Toast.makeText(getApplicationContext(), "Sunucuda Hata", Toast.LENGTH_SHORT).show();
                }else if(error instanceof AuthFailureError){
                    Toast.makeText(getApplicationContext(), "Başarısız Durum", Toast.LENGTH_SHORT).show();
                }
            }
        });
        BenimVolleyim.getmInstance(getApplicationContext()).addToRequestque(jsonArrayRequest);
    }//METHOD

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
                     menuInflater.inflate(R.menu.menu_urunler,menu);

        SubMenu mt = menu.getItem(2).getSubMenu();
        mt.getItem(0).setIcon(R.mipmap.menu_isaretle_icon);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menu_geridon){
            Intent t = new Intent(UrunlerActivity_Aktif.this,MainActivity.class);
                    startActivity(t);
        }else if(item.getItemId() == R.id.menu_ekle){
            Intent t = new Intent(UrunlerActivity_Aktif.this,UrunekleActivity.class);
                    startActivity(t);
        }

            switch (item.getItemId()){
                case R.id.menu_aktif_listele:

                   /*
                    mPrefsEditor.putInt("secim",1);
                    mPrefsEditor.commit();
                    */

                   //startActivity(new Intent(UrunlerActivity_Aktif.this,UrunlerActivity_Aktif.class));
                    break;
                case R.id.menu_pasif_listele:
                    /*
                    mPrefsEditor.putInt("secim",0);
                    mPrefsEditor.commit();
                    */
                    startActivity(new Intent(UrunlerActivity_Aktif.this,UrunlerActivity_Pasif.class));
                    break;
                case R.id.menu_hepsi_listele:

                    /*
                    mPrefsEditor.putInt("secim",2);
                    mPrefsEditor.commit();
                    */
                    startActivity(new Intent(UrunlerActivity_Aktif.this,UrunlerActivity_Hepsi.class));
                    break;
            }//
        return true;
    }


    public interface CallBack{
        void onSuccess(List<ProductClass> productClassList);
        void onFail(String msg);
    }//INTERFACE

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

