package com.bsmstudyo.vebateknoloji.uruntakipsistemi2;

import android.Manifest;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrunekleActivity extends Activity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{

    ImageButton imageButton1 ,imageButton2,imageButton3;
    Button btn_kaydet;
    EditText edt_urunismi,edt_birimfiyati,edt_urunkodu;
    RadioGroup rd_grp_kategori_ekle,rd_grp_aktif_pasif;
    private Bitmap bitmap_image1,bitmap_image2,bitmap_image3;
    private  RadioButton b,rdb_aktif,rdb_pasif;

    CharSequence secilenKategoriler;
    List<KategoriClass> listKategori;
    int secilenID=0;
    int UrunDurumu;

    boolean kategori_secildiMI = false;
    boolean yayin_secildiMI=false;

    public static int TAKE_CAPTURE_1 = 1;
    public static int TAKE_CAPTURE_2 = 2;
    public static int TAKE_CAPTURE_3 = 3;

    public static int TAKE_GALERIA_1 = 48;
    public static int TAKE_GALERIA_2 = 9;
    public static int TAKE_GALERIA_3 = 35;

    ProgressDialog dialog=null;


    private final String INSERT_URL = "INSERT PRODUCUTS SERVISI";
    private final String DINAMIK_KATEGORI = "SHOW CATEGORI SERVISI";
    private final String KOD_YARAT_LINKI = "PRODUCTS CREATE CODE SERVISI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_urunekle);

        listKategori = new ArrayList<KategoriClass>();

            CREATE_OBJECTS();
            Categorileri_GETIR();

    }//onCREATE

    public void CREATE_OBJECTS() {
        rdb_aktif = (RadioButton)findViewById(R.id.rdb_aktif);
        rdb_pasif = (RadioButton)findViewById(R.id.rdb_pasif);
        rdb_aktif.setOnClickListener(this);
        rdb_pasif.setOnClickListener(this);

        rd_grp_kategori_ekle = (RadioGroup)findViewById(R.id.rd_grp_kategori_ekle);
        rd_grp_aktif_pasif = (RadioGroup)findViewById(R.id.rd_grp_aktif_pasif);
        rd_grp_kategori_ekle.setOnCheckedChangeListener(this);

        edt_urunismi = (EditText)findViewById(R.id.edt_urunIsmi);
        edt_birimfiyati = (EditText)findViewById(R.id.edt_BirimFiyati);
        edt_urunkodu = (EditText)findViewById(R.id.edt_urunKodu);
        edt_urunkodu.setEnabled(false);

        btn_kaydet = (Button)findViewById(R.id.btn_kaydet);
        imageButton1 = (ImageButton)findViewById(R.id.urun_resmiBir);
        imageButton2 = (ImageButton)findViewById(R.id.urun_resmiiki);
        imageButton3 = (ImageButton)findViewById(R.id.urun_resmiUc);
        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        btn_kaydet.setOnClickListener(this);
    }//func
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.urun_resmiBir:
                RESIM_EKLEME_SURECI(TAKE_CAPTURE_1,TAKE_GALERIA_1);
                break;
            case R.id.urun_resmiiki:
                RESIM_EKLEME_SURECI(TAKE_CAPTURE_2,TAKE_GALERIA_2);
                break;
            case R.id.urun_resmiUc:
               RESIM_EKLEME_SURECI(TAKE_CAPTURE_3,TAKE_GALERIA_3);
                break;
            case R.id.btn_kaydet:
              //  UrunKAYDETIslemi();
                islemiYap();
                break;
            case R.id.rdb_aktif:
               UrunDurumu = 1;
               // Toast.makeText(UrunekleActivity.this,"AKT"+UrunDurumu,Toast.LENGTH_LONG).show();
                break;
            case R.id.rdb_pasif:
                UrunDurumu = 0;
               // Toast.makeText(UrunekleActivity.this,"PAS"+UrunDurumu,Toast.LENGTH_LONG).show();
                break;
        }//SWİTCH
    }//ONCLİCK FUNC ->

    public void RESIM_EKLEME_SURECI(final int KAMERA_SABITI,final int GALERI_SABITI){
        AlertDialog.Builder secenek1 = new AlertDialog.Builder(UrunekleActivity.this);
        secenek1.setTitle("Resim Yükleme Seçeneği");
        secenek1.setMessage("Resminizi Uygulamanıza eklemek için tercih yapmalısınız.");
        secenek1.setPositiveButton("Galeri'den", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GALERI_AND_CAMERA(GALERI_SABITI);
            }
        });
        secenek1.setNeutralButton("Kamera'dan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    islemiYap2(KAMERA_SABITI);
            }
        });

        AlertDialog alertDialog = secenek1.create();
        alertDialog.show();
    }//METHOD ->

    public void GALERI_AND_CAMERA(final int GALERI_SABITI){
        Intent intentgaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               intentgaleria.setType("image/*");
               startActivityForResult(intentgaleria,GALERI_SABITI);
    }//FUNC->

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TAKE_CAPTURE_1){
           // KAMERADAN_RESMI_CEKME(data,bitmap_image1,imageButton1);

            if(data != null) {
                Bundle bndle = data.getExtras();
                bitmap_image1 = (Bitmap) bndle.get("data");
                imageButton1.setImageBitmap(bitmap_image1);
            }
        }else if(requestCode == TAKE_CAPTURE_2){
            //KAMERADAN_RESMI_CEKME(data,bitmap_image2,imageButton2);

            if(data !=  null) {
                Bundle bndle = data.getExtras();
                bitmap_image2 = (Bitmap) bndle.get("data");
                imageButton2.setImageBitmap(bitmap_image2);
            }
        }else if(requestCode == TAKE_CAPTURE_3){
          // KAMERADAN_RESMI_CEKME(data,bitmap_image3,imageButton3);
            if(data != null) {
                Bundle bndle = data.getExtras();
                bitmap_image3 = (Bitmap) bndle.get("data");
                imageButton3.setImageBitmap(bitmap_image3);
            }
        }else if(requestCode == TAKE_GALERIA_1){
            //GALERIDEKI_RESMI_CEKME(data,bitmap_image1,imageButton1);

            if(data != null) {
                Uri selectedImage = data.getData();
                String[] filepathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filepathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filepathColumn[0]);
                String filepath = cursor.getString(columnIndex);
                bitmap_image1 = BitmapFactory.decodeFile(filepath);
                imageButton1.setImageDrawable(getResources().getDrawable(R.mipmap.icon_resimonay, null));
                cursor.close();
            }

        }else if(requestCode == TAKE_GALERIA_2){
          //  GALERIDEKI_RESMI_CEKME(data,bitmap_image2,imageButton2);

            if(data != null) {
                Uri selectedImage = data.getData();
                String[] filepathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filepathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filepathColumn[0]);
                String filepath = cursor.getString(columnIndex);
                bitmap_image2 = BitmapFactory.decodeFile(filepath);
                imageButton2.setImageDrawable(getResources().getDrawable(R.mipmap.icon_resimonay, null));
                cursor.close();
            }
        }else if(requestCode == TAKE_GALERIA_3){
             //GALERIDEKI_RESMI_CEKME(data,bitmap_image3,imageButton3);

            if(data != null) {
                Uri selectedImage = data.getData();
                String[] filepathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filepathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filepathColumn[0]);
                String filepath = cursor.getString(columnIndex);
                bitmap_image3 = BitmapFactory.decodeFile(filepath);
                imageButton3.setImageDrawable(getResources().getDrawable(R.mipmap.icon_resimonay, null));

                cursor.close();
            }
        }
    }//onActivittyResult

    // ANA kullanacağın fonksiyon bu
    public void islemiYap() {
        if (checkPermission()) {
        UrunKAYDETIslemi();
        } else {
            requestPermission();
        }
    }

    public void islemiYap2(final int KAMERA_SABITI) {
        if(checkPermission2()){
            Intent picture_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(picture_intent,KAMERA_SABITI);
        }else{
            requestPermission2();
        }
    }
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE2 = 2;

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(UrunekleActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(UrunekleActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(UrunekleActivity.this, "Read External Storage permission allows us to do store log files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(UrunekleActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission2() {
        int result = ContextCompat.checkSelfPermission(UrunekleActivity.this, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission2() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(UrunekleActivity.this, Manifest.permission.CAMERA)) {
            Toast.makeText(UrunekleActivity.this, "Read External Storage permission allows us to do store log files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(UrunekleActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE2);
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

    public void KAMERADAN_RESMI_CEKME(Intent data, Bitmap bitmap, ImageButton imageButton){
        if(data != null) {
            Bundle bndle = data.getExtras();
            bitmap = (Bitmap) bndle.get("data");
            imageButton.setImageBitmap(bitmap);
        }
    }//FUNC->
    public void GALERIDEKI_RESMI_CEKME(Intent data,Bitmap bitmap,ImageButton imageButton){
        Uri selectedImage =  data.getData();
        String[] filepathColumn =  {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage,filepathColumn,null,null,null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filepathColumn[0]);
        String filepath = cursor.getString(columnIndex);
        bitmap = BitmapFactory.decodeFile(filepath);
        imageButton.setImageBitmap(bitmap);
        cursor.close();

    }//FUNCTION ->

    public void UrunKAYDETIslemi(){


        final String urunAdi = edt_urunismi.getText().toString().trim();
        final String urunKodu = edt_urunkodu.getText().toString().trim();
        final String birimFiyati = edt_birimfiyati.getText().toString().trim();


            int yayin_comp = rd_grp_aktif_pasif.getChildCount();

        for(int i=0;i<yayin_comp;i++){
            View v = rd_grp_aktif_pasif.getChildAt(i);
            if(v instanceof RadioButton){
                if(((RadioButton) v).isChecked() == true){
                    yayin_secildiMI = true;
                    break;
                }else{
                    yayin_secildiMI = false;
                    continue;
                }
            }// PARENT IF
        }//FOR


        int kategori_comp = rd_grp_kategori_ekle.getChildCount();

        for(int m=0;m<kategori_comp;m++){
            View e = rd_grp_kategori_ekle.getChildAt(m);
            if(e instanceof RadioButton){
                if(((RadioButton)e).isChecked() == true){
                    kategori_secildiMI = true;
                    break;
                }else{
                    kategori_secildiMI = false;
                    continue;
                }
            }//PARENT IF
            break;
        }//FOR

       if(bitmap_image1 != null /*& bitmap_image2 != null & bitmap_image3 != null*/
                & yayin_secildiMI != false & kategori_secildiMI !=false /*& urunAdi.length() >=3*/){

           Toast.makeText(getApplicationContext(),"HERSEY DOGRU"+kategori_secildiMI,Toast.LENGTH_LONG).show();


        dialog = new ProgressDialog(UrunekleActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Ürününüz Sisteminize Yükleniyor Lütfen Bekleyiniz..");
        dialog.show();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSERT_URL, new Response.Listener() {
            @Override
            public void onResponse(Object response) {


                //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                AlertDialog.Builder eklendiMesaji = new AlertDialog.Builder(UrunekleActivity.this);
                                    eklendiMesaji.setTitle("Başarılı");
                                    eklendiMesaji.setMessage("Ürününüz Başarıyla Sisteminize Eklendi.");
                                    eklendiMesaji.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //BIRSEYLER YAPARSIN SONRADAN
                                            finish();
                                        }
                                    });
                AlertDialog alert = eklendiMesaji.create();
                            dialog.dismiss();
                            alert.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                BenimVolleyim.HATALARI_YAKALA(error,"VERI KAYIT SORUNU");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                                   params.put("urun_ismi",urunAdi);
                                   params.put("birim_fiyati",birimFiyati);
                                   params.put("urun_kodu",urunKodu);
                                   params.put("secilen_kategori",""+secilenID);
                                   params.put("urun_yayinlama_durumu",""+UrunDurumu);
                if(bitmap_image1 != null) {
                    params.put("resim_kodu", imageToString(bitmap_image1));
                }
                if(bitmap_image2 != null) {
                    params.put("resim_kodu2", imageToString(bitmap_image2));
                }
                if(bitmap_image3 != null) {
                    params.put("resim_kodu3", imageToString(bitmap_image3));
                }

                return params;
            }
        };
        //TimeoutErrror hatası alıyordum bu KURALI Vererek ZAMAN ASIMI HATASINI ALMAMAK ICIN UZATTIM YUKLEME ZAMANINI
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                   DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ));

        BenimVolleyim.getmInstance(getApplicationContext()).addToRequestque(stringRequest);


        } else{
           if(dialog != null) {
               dialog.dismiss();
           }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setTitle("Verilerini Gözden Geçir.");
                                builder.setMessage("Yanlış veya Eksik Veri Girdiniz. Lütfen Kontrol ediniz");
                builder.setNegativeButton("Tekrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"tkrar",Toast.LENGTH_LONG).show();
                    }
                });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }//FUNCTION ->

    public String imageToString(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }//method

    public void Categorileri_GETIR(){
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

                for(int m=0;m<listKategori.size();m++){
                    //RADIOBUTTON NESNESI OLUŞTURULUP ICINE NESNELER YERLEŞTİRİLİYOR
                    b = new RadioButton(UrunekleActivity.this);
                    b.setText(listKategori.get(m).getCategori_adi());
                    rd_grp_kategori_ekle.addView(b);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                       BenimVolleyim.HATALARI_YAKALA(error,"KATEGORI GETIRME SORUNU");
            }
        });
        BenimVolleyim.getmInstance(getApplicationContext()).addToRequestque(jsonArrayRequest);
    }//func

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {


        int cou = rd_grp_kategori_ekle.getChildCount();
            for(int i=0;i<cou;i++){
                   final View o = rd_grp_kategori_ekle.getChildAt(i);
                if(o instanceof RadioButton){
                    o.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                             secilenKategoriler = ((RadioButton) o).getText().toString();

                            for(int i=0; i < listKategori.size();i++){
                                if(listKategori.get(i).getCategori_adi() == secilenKategoriler.toString()){
                                    secilenID = listKategori.get(i).getCategori_id();
                                    break;
                                }
                            }
                           URUN_KODU_YARAT(secilenKategoriler.toString());
                        }
                    });
                }
            }//for
    }//METHOD-->

    public void URUN_KODU_YARAT(final CharSequence code){

        StringRequest kodYara = new StringRequest(Request.Method.POST, KOD_YARAT_LINKI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                edt_urunkodu.setText(response);
                //Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

               BenimVolleyim.HATALARI_YAKALA(error,"KOD URETME SORUNU");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> mt = new HashMap<String,String>();
                                   mt.put("ktg_scm",""+code);
                return mt;
            }
        };
        BenimVolleyim.getmInstance(UrunekleActivity.this).addToRequestque(kodYara);

    }//FUNCTION

}
