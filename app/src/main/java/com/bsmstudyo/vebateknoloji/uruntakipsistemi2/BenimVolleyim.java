package com.bsmstudyo.vebateknoloji.uruntakipsistemi2;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class BenimVolleyim  {

    private static BenimVolleyim mInstance;
    private RequestQueue requestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;
    private static ImageLoader sImageLoader;


    private BenimVolleyim(Context context){

        mCtx = context;
        requestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {

                    LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(5000);
                   // private LruBitmapCache cache = new LruBitmapCache();
                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.resize(4024);
                        cache.put(url, bitmap);
                    }
                });
    }

        public RequestQueue getRequestQueue(){
            if(requestQueue == null){
                requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
            }
            return requestQueue;
        }


        public static synchronized  BenimVolleyim getmInstance(Context context){
            if(mInstance == null){
                mInstance = new BenimVolleyim(context);
            }
            return mInstance;
        }


        public <T>void addToRequestque(Request<T> request){
            requestQueue.add(request);
        }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    //VOLLEY HATALARINI YAKALAMAK ICIN YAZILIM FUNCTION
    public static void HATALARI_YAKALA(VolleyError error,String text){

        if(error instanceof NoConnectionError){
            Toast.makeText(mCtx, "Internet erişimi bulunamadı..", Toast.LENGTH_SHORT).show();
        }else if(error instanceof TimeoutError){
            Toast.makeText(mCtx, "Zaman Aşımı Hatası", Toast.LENGTH_SHORT).show();
        }else if(error instanceof ParseError){
            Toast.makeText(mCtx, "Veriler Gelirken bir Hata oluştu.", Toast.LENGTH_SHORT).show();
        }else if(error instanceof NetworkError){
            Toast.makeText(mCtx, "Network Hatası.", Toast.LENGTH_SHORT).show();
        }else if(error instanceof ServerError){
            Toast.makeText(mCtx, "Sunucuda Hata "+text, Toast.LENGTH_SHORT).show();
        }else if(error instanceof AuthFailureError){
            Toast.makeText(mCtx, "Başarısız Durum", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(mCtx, text, Toast.LENGTH_SHORT).show();
        }
    }//FUNC->

}
