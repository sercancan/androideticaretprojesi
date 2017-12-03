package com.bsmstudyo.vebateknoloji.uruntakipsistemi2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UrunDuzenleActivity extends Activity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{

    Button btn_kaydet_DUZENLE;
    EditText edt_urunismi_DUZENLE,edt_birimfiyati_DUZENLE,edt_urunkodu_DUZENLE;
    RadioGroup radio_group1_DUZENLE;
    private Bitmap bitmap_image1_DUZENLE,bitmap_image2_DUZENLE,bitmap_image3_DUZENLE,bitmap_urun_resmi_genel;
    private  RadioButton b_DUZENLE,rdb_aktif_DUZENLE,rdb_pasif_DUZENLE;
    private ImageButton img_btn_resim1,img_btn_resim2,img_btn_resim3;
    private boolean resim_durum1 =false;
    private boolean resim_durum2 = false;
    private boolean resim_durum3 = false;

    ImageView urun_resmi_genel;

    private int yayinDurumu;
    private int kategoriDurumu;
    private int product_id;
   // private String resimYolu;
    String gelen_urunKodu;
    private CharSequence secilenKategoriler;

    ProgressDialog progressDialog = null;

    private final String DINAMIK_KATEGORI = "SHOW CATEGORI SERVISI";
    private final String UPDATE_VERI = "UPDATE PRODUCTS SERVISI";
    List<KategoriClass> listKategori;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_urunduzenle);

        listKategori = new ArrayList<KategoriClass>();


        CREATE_OBJECTS();
        GET_DATAS();

    }//ONCREATE

    public void CREATE_OBJECTS(){

        img_btn_resim1 = (ImageButton)findViewById(R.id.urunduz_resmiBir);
        img_btn_resim2 = (ImageButton)findViewById(R.id.urunduz_resmiiki);
        img_btn_resim3 = (ImageButton)findViewById(R.id.urunduz_resmiUc);

        img_btn_resim1.setOnClickListener(this);
        img_btn_resim2.setOnClickListener(this);
        img_btn_resim3.setOnClickListener(this);

        urun_resmi_genel = (ImageView)findViewById(R.id.urun_resmi_genel);

        rdb_aktif_DUZENLE = (RadioButton)findViewById(R.id.rdb_aktif_DUZENLE);
        rdb_pasif_DUZENLE = (RadioButton)findViewById(R.id.rdb_pasif_DUZENLE);
        rdb_aktif_DUZENLE.setOnClickListener(this);
        rdb_pasif_DUZENLE.setOnClickListener(this);

        radio_group1_DUZENLE = (RadioGroup)findViewById(R.id.radio_group1_DUZENLE);
        radio_group1_DUZENLE.setOnCheckedChangeListener(this);

        edt_urunismi_DUZENLE = (EditText)findViewById(R.id.edt_urunIsmi_DUZENLE);
        edt_birimfiyati_DUZENLE = (EditText)findViewById(R.id.edt_BirimFiyati_DUZENLE);
        edt_urunkodu_DUZENLE = (EditText)findViewById(R.id.edt_urunKodu_DUZENLE);
        edt_urunkodu_DUZENLE.setEnabled(false);

        btn_kaydet_DUZENLE = (Button)findViewById(R.id.btn_kaydet_DUZENLE);
        btn_kaydet_DUZENLE.setOnClickListener(this);

        progressDialog = new ProgressDialog(UrunDuzenleActivity.this);


    }//METHOD ->

    public void GET_DATAS(){
        if(getIntent().getExtras().getString("urun_Ismi") != null && getIntent().getExtras().getString("urun_Duzenle") != null){

            String gelen_urunIsmi = getIntent().getExtras().getString("urun_Ismi").toString();
             gelen_urunKodu = getIntent().getExtras().getString("urun_Duzenle").toString();
            String gelen_urunFiyat = getIntent().getExtras().getString("urun_birimFiyati").toString();
           String resimYolu = getIntent().getExtras().getString("resim_yolu").toString();

            kategoriDurumu = getIntent().getExtras().getInt("kategori_id");
            yayinDurumu = getIntent().getExtras().getInt("yayinDurumu");
            product_id = getIntent().getExtras().getInt("prdct_ID");

            edt_urunismi_DUZENLE.setText(gelen_urunIsmi);
            edt_urunkodu_DUZENLE.setText(gelen_urunKodu);
            edt_birimfiyati_DUZENLE.setText(gelen_urunFiyat);

            Picasso.with(UrunDuzenleActivity.this).invalidate(resimYolu);
            Picasso.with(UrunDuzenleActivity.this).load(resimYolu)
                                                  .networkPolicy(NetworkPolicy.NO_CACHE)
                                                   .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(urun_resmi_genel);

            if(yayinDurumu == 1){
                rdb_aktif_DUZENLE.setChecked(true);
            }else if(yayinDurumu == 0) {
                rdb_pasif_DUZENLE.setChecked(true);
            }

            //Toast.makeText(UrunDuzenleActivity.this,resimYolu,Toast.LENGTH_LONG).show();
            Categorileri_GETIR(kategoriDurumu);
        }
    }//FUNCTION->

    public void Categorileri_GETIR(final int kategoriID){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, DINAMIK_KATEGORI, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                int count = 0;
                while(count < response.length()){
                    try{

                        JSONObject obj = response.getJSONObject(count);
                        KategoriClass ktgriSinifi = new KategoriClass();
                        ktgriSinifi.setCategori_id(obj.getInt("categori_id"));
                        ktgriSinifi.setCategori_adi(obj.getString("kategori"));

                        ///LISTEYE KATEGORILER EKLENIYOR
                        listKategori.add(ktgriSinifi);

                        count++;
                    }catch (JSONException er){
                        Toast.makeText(getApplicationContext(),"JSONARRAYREQUEST HATA",Toast.LENGTH_LONG).show();
                    }
                }//WHILE

                for(KategoriClass k :listKategori ){

                    b_DUZENLE = new RadioButton(UrunDuzenleActivity.this);
                    b_DUZENLE.setText(k.getCategori_adi());
                    radio_group1_DUZENLE.addView(b_DUZENLE);
                    if(k.getCategori_id() == kategoriID) {
                        b_DUZENLE.setChecked(true);
                    }
               //Toast.makeText(UrunDuzenleActivity.this,k.getCategori_adi(),Toast.LENGTH_LONG).show();
               }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UrunDuzenleActivity.this,"KATEGORI GETIRME SORUNU",Toast.LENGTH_LONG).show();
            }
        });
        BenimVolleyim.getmInstance(getApplicationContext()).addToRequestque(jsonArrayRequest);
    }//FUNCTION

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btn_kaydet_DUZENLE){
            btn_kaydet_DUZENLE.setEnabled(false);
            islemiYap();
          //  Toast.makeText(getApplicationContext(),"BT",Toast.LENGTH_LONG).show();
        }
        if(v.getId() == R.id.rdb_aktif_DUZENLE){
            yayinDurumu = 1;
           // Toast.makeText(getApplicationContext(),"AK",Toast.LENGTH_LONG).show();

        }if(v.getId() == R.id.rdb_pasif_DUZENLE){
            yayinDurumu = 0;
           // Toast.makeText(getApplicationContext(),"PAF",Toast.LENGTH_LONG).show();
        }
        if(v.getId() == R.id.urunduz_resmiBir){
            GALERI_GIDIYO(21);
        }if(v.getId() == R.id.urunduz_resmiiki){
            GALERI_GIDIYO(22);
        }if(v.getId() == R.id.urunduz_resmiUc){
            GALERI_GIDIYO(23);
        }
    }
public void GALERI_GIDIYO(int ISTEK){
    Intent intentgaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    intentgaleria.setType("image/*");
    startActivityForResult(intentgaleria,ISTEK);
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 21){
              //  GALERIDEKI_RESMI_CEKME(data,bitmap_image1_DUZENLE,img_btn_resim1);
            if(data != null) {
                Uri selectedImage = data.getData();
                String[] filepathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filepathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filepathColumn[0]);
                String filepath = cursor.getString(columnIndex);
                bitmap_image1_DUZENLE = BitmapFactory.decodeFile(filepath);
                img_btn_resim1.setImageDrawable(getResources().getDrawable(R.mipmap.icon_resimonay, null));
                cursor.close();
                resim_durum1 = true;
            }
        }else if(requestCode == 22){
           // GALERIDEKI_RESMI_CEKME(data,bitmap_image2_DUZENLE,img_btn_resim2);
            if(data != null) {
                Uri selectedImage = data.getData();
                String[] filepathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filepathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filepathColumn[0]);
                String filepath = cursor.getString(columnIndex);
                bitmap_image2_DUZENLE = BitmapFactory.decodeFile(filepath);
                img_btn_resim2.setImageDrawable(getResources().getDrawable(R.mipmap.icon_resimonay, null));
                cursor.close();
                resim_durum2 = true;
            }
        }else if(requestCode == 23){
          //  GALERIDEKI_RESMI_CEKME(data,bitmap_image3_DUZENLE,img_btn_resim3);
            if(data != null) {
                Uri selectedImage = data.getData();
                String[] filepathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filepathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filepathColumn[0]);
                String filepath = cursor.getString(columnIndex);
                bitmap_image3_DUZENLE = BitmapFactory.decodeFile(filepath);
                img_btn_resim3.setImageDrawable(getResources().getDrawable(R.mipmap.icon_resimonay, null));
                cursor.close();
                resim_durum3 = true;
            }
        }

    }//FOR

    public void GALERIDEKI_RESMI_CEKME(Intent data, Bitmap bitmap, ImageButton imageButton){
        Uri selectedImage =  data.getData();
        String[] filepathColumn =  {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage,filepathColumn,null,null,null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filepathColumn[0]);
        String filepath = cursor.getString(columnIndex);
        bitmap = BitmapFactory.decodeFile(filepath);
        imageButton.setImageDrawable(getResources().getDrawable(R.mipmap.icon_resimonay,null));
        cursor.close();

    }//FUNCTION ->

    public void DATA_UPDATE_SAVE(){
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Ürün Güncelleme Yapılıyor. Lütfen Bekleyiniz..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_VERI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Toast.makeText(UrunDuzenleActivity.this,response.toString(), Toast.LENGTH_LONG).show();
                AlertDialog.Builder GuncellediMesaji = new AlertDialog.Builder(UrunDuzenleActivity.this);
                GuncellediMesaji.setTitle("Başarılı");
                GuncellediMesaji.setMessage("Ürününüz Başarıyla Güncellendi..");
                GuncellediMesaji.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //BIRSEYLER YAPARSIN SONRADAN
                        btn_kaydet_DUZENLE.setEnabled(true);
                        finish();
                    }
                });
                AlertDialog alert = GuncellediMesaji.create();
                alert.show();
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                BenimVolleyim.HATALARI_YAKALA(error,"Guncelleme SIKINTI");

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String,String>();
                params.put("urun_kodu",""+gelen_urunKodu);
                params.put("urun_ismi",edt_urunismi_DUZENLE.getText().toString());
                params.put("birim_fiyati",edt_birimfiyati_DUZENLE.getText().toString());
                params.put("secilen_kategori",""+kategoriDurumu);
                params.put("urun_yayinlama_durumu",""+yayinDurumu);
                params.put("prdct_ID",""+product_id);

                if(resim_durum1==true) {
                    params.put("resim1", imageToString(bitmap_image1_DUZENLE));
                }
                if(resim_durum2==true) {
                    params.put("resim2", imageToString(bitmap_image2_DUZENLE));
                }
                if(resim_durum3==true) {
                    params.put("resim3", imageToString(bitmap_image3_DUZENLE));
                }
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ));
        BenimVolleyim.getmInstance(getApplicationContext()).addToRequestque(stringRequest);
    }
       @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
           int cou = radio_group1_DUZENLE.getChildCount();
           for(int i=0;i<cou;i++){
               final View o = radio_group1_DUZENLE.getChildAt(i);
               if(o instanceof RadioButton){
                   o.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           secilenKategoriler = ((RadioButton) o).getText().toString();

                           for(int i=0; i < listKategori.size();i++){
                               if(listKategori.get(i).getCategori_adi() == secilenKategoriler.toString()){
                                   kategoriDurumu = listKategori.get(i).getCategori_id();
                                   break;
                               }
                           }
                           Toast.makeText(UrunDuzenleActivity.this,kategoriDurumu+"-"+yayinDurumu,Toast.LENGTH_LONG).show();

                       }
                   });
               }
           }//for
    }

    public String imageToString(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }//method

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE2 = 2;

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(UrunDuzenleActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(UrunDuzenleActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(UrunDuzenleActivity.this, "Read External Storage permission allows us to do store log files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(UrunDuzenleActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                    // izin alındı şimdi yapmak istediğin işlemi yapabilirsin
                    // invoke etmek istediğin fonksiyonu burda çağır
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;

            case PERMISSION_REQUEST_CODE2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                    // izin alındı şimdi yapmak istediğin işlemi yapabilirsin
                    // invoke etmek istediğin fonksiyonu burda çağır
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    public void islemiYap() {
        if (checkPermission()) {
            DATA_UPDATE_SAVE();
        } else {
            requestPermission();
        }
    }
}
