package com.bsmstudyo.vebateknoloji.uruntakipsistemi2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class Urunler_Adapter extends RecyclerView.Adapter<Urunler_Adapter.ViewUrunler> {

       private List<ProductClass> productlist;
       private Context context;
       private String resimyolu="";

    public Urunler_Adapter(List<ProductClass> productlist, Context context) {
        this.productlist = productlist;
        this.context = context;
    }

    @Override
    public ViewUrunler onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);
        ViewUrunler viewUrunler = new ViewUrunler(v);
        return viewUrunler;
    }

    @Override
    public void onBindViewHolder(final ViewUrunler holder, int position) {

        ProductClass productClass = productlist.get(position);

        ImageLoader mImageLoader = BenimVolleyim.getmInstance(this.context).getImageLoader();
            holder.networkImageView.setImageUrl(productClass.getResimYolu().trim().toString(),mImageLoader);
            holder.txt_urunkodu.setText(productClass.getUrunKodu());
            holder.txt_urunismi.setText(productClass.getUrunAdi());
            holder.txt_urunEklenmeTarihi.setText(productClass.getTarih());

            holder.txt_urunFiyati.setText(""+productClass.getFiyat());
            holder.txt_KategoriId.setText(""+productClass.getKategori_id());
            holder.txt_yayinDurumu.setText(""+productClass.getYayinDurumu());
            holder.txt_urun_benzersiz_no.setText(""+productClass.getNumara());

            holder.txt_resimyolu.setText(productClass.getResimYolu());


        int veri =  Integer.parseInt(holder.txt_yayinDurumu.getText().toString());

            if(veri == 1){
                holder.img_yayin_aktif.setVisibility(View.INVISIBLE);
                holder.img_yayin_pasif.setVisibility(View.VISIBLE);

            } else if (veri == 0) {
                holder.img_yayin_aktif.setVisibility(View.VISIBLE);
                holder.img_yayin_pasif.setVisibility(View.INVISIBLE);
            }

        //BU VERILERI TextView içerisinde gizliyorum yarın öbür gün lazım oolur diye artık
        holder.txt_urunFiyati.setVisibility(View.INVISIBLE);
        holder.txt_KategoriId.setVisibility(View.INVISIBLE);
        holder.txt_yayinDurumu.setVisibility(View.INVISIBLE);
        holder.txt_urun_benzersiz_no.setVisibility(View.INVISIBLE);
        holder.txt_resimyolu.setVisibility(View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return this.productlist.size();
    }

    public class ViewUrunler extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ImageView img_yayin_aktif,img_yayin_pasif;
        public TextView txt_urunkodu,txt_urunismi,txt_urunEklenmeTarihi,txt_urunFiyati,txt_yayinDurumu,
                txt_KategoriId,txt_urun_benzersiz_no,txt_resimyolu;
        public NetworkImageView networkImageView;

        public ViewUrunler(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            img_yayin_aktif = (ImageView)itemView.findViewById(R.id.img_yayin_aktif);
            img_yayin_pasif = (ImageView)itemView.findViewById(R.id.img_yayin_pasif);

            networkImageView = (NetworkImageView)itemView.findViewById(R.id.imgnetwork_resim_goster);
            txt_urunkodu = (TextView)itemView.findViewById(R.id.txt_urunKodu);
            txt_urunismi = (TextView)itemView.findViewById(R.id.txt_UrunAdi);
            txt_urunEklenmeTarihi = (TextView)itemView.findViewById(R.id.txt_urunEklenmeTarihi);
            txt_urunFiyati = (TextView)itemView.findViewById(R.id.txt_urunFiyati);
            txt_yayinDurumu =  (TextView) itemView.findViewById(R.id.txt_yayin_durumu);
            txt_KategoriId = (TextView)itemView.findViewById(R.id.txt_kategori_id);
            txt_urun_benzersiz_no = (TextView)itemView.findViewById(R.id.txt_urun_benzersiz_no);
            txt_resimyolu = (TextView)itemView.findViewById(R.id.txt_resim_url);


        }

        @Override
        public void onClick(View v) {

            Intent intent =  new Intent(context.getApplicationContext(),UrunDuzenleActivity.class);

                   intent.putExtra("urun_Duzenle",txt_urunkodu.getText().toString());
                   intent.putExtra("urun_Ismi",txt_urunismi.getText().toString());
                   intent.putExtra("urun_birimFiyati",txt_urunFiyati.getText().toString());
                   intent.putExtra("yayin_Durmu",txt_yayinDurumu.getText().toString());
                   intent.putExtra("resim_yolu",txt_resimyolu.getText().toString());

            int prdct_id = Integer.parseInt(txt_urun_benzersiz_no.getText().toString());
                   intent.putExtra("prdct_ID",prdct_id);
            int katagoriID = Integer.parseInt(txt_KategoriId.getText().toString());
                   intent.putExtra("kategori_id",katagoriID);
            int yayinDurumu = Integer.parseInt(txt_yayinDurumu.getText().toString());
                    intent.putExtra("yayinDurumu",yayinDurumu);


            context.startActivity(intent);
        }

        public String imageToString(Bitmap bitmap){

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte[] imgBytes = byteArrayOutputStream.toByteArray();

            return Base64.encodeToString(imgBytes,Base64.DEFAULT);
        }//method


    }//CLASS SUB CLASS



}
