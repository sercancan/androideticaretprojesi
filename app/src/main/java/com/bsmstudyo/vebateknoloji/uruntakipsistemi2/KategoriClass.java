package com.bsmstudyo.vebateknoloji.uruntakipsistemi2;


import android.content.Context;

public class KategoriClass {

    private int categori_id;
    private String categori_adi;
    private Context context;

    public KategoriClass(){

    }

    public KategoriClass(Context ctx){
        this.context = ctx;
    }

    public int getCategori_id() {
        return categori_id;
    }

    public void setCategori_id(int categori_id) {
        this.categori_id = categori_id;
    }

    public String getCategori_adi() {
        return categori_adi;
    }

    public void setCategori_adi(String categori_adi) {
        this.categori_adi = categori_adi;
    }


}
