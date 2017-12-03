package com.bsmstudyo.vebateknoloji.uruntakipsistemi2;

/**
 * Created by vebateknoloji on 30.06.2017.
 */

public class ProductClass {


    private int numara;
    private String urunAdi;
    private String urunKodu;
    private String UrunKayitTarihi;
    private String resimYolu;
    private int fiyat;
    private String tarih;
    private int yayinDurumu;
    private int kategori_id;

    private int menu_liste_secimi;
    public ProductClass() {

    }

    public int getNumara() {
        return numara;
    }

    public void setNumara(int numara) {
        this.numara = numara;
    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public void setUrunAdi(String urunAdi) {
        this.urunAdi = urunAdi;
    }

    public String getUrunKodu() {
        return urunKodu;
    }

    public void setUrunKodu(String urunKodu) {
        this.urunKodu = urunKodu;
    }

    public String getUrunKayitTarihi() {
        return UrunKayitTarihi;
    }

    public void setUrunKayitTarihi(String urunKayitTarihi) {
        UrunKayitTarihi = urunKayitTarihi;
    }

    public String getResimYolu() {
        return resimYolu;
    }

    public void setResimYolu(String resimYolu) {
        this.resimYolu = resimYolu;
    }

    public int getFiyat() {
        return fiyat;
    }

    public void setFiyat(int fiyat) {
        this.fiyat = fiyat;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public int getYayinDurumu() {
        return yayinDurumu;
    }

    public void setYayinDurumu(int yayinDurumu) {
        this.yayinDurumu = yayinDurumu;
    }

    public int getKategori_id() {
        return kategori_id;
    }

    public void setKategori_id(int kategori_id) {
        this.kategori_id = kategori_id;
    }

    public int getMenu_liste_secimi() {
        return menu_liste_secimi;
    }

    public void setMenu_liste_secimi(int menu_liste_secimi) {
        this.menu_liste_secimi = menu_liste_secimi;
    }
}
