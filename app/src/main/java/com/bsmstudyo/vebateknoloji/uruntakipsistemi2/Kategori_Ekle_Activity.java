package com.bsmstudyo.vebateknoloji.uruntakipsistemi2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class Kategori_Ekle_Activity extends AppCompatActivity implements View.OnClickListener {

    EditText edt_kategori_ismi;
    Button btn_kategori;
    TextView textView;

    private final String KATEGORI_RESTFUL_LINK = "INSERT KATEGORI SERVISI";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_kategoriekle);

        textView = (TextView)findViewById(R.id.gozysi);
        edt_kategori_ismi = (EditText)findViewById(R.id.edt_kategori_ismi);
        btn_kategori = (Button)findViewById(R.id.btn_kategori_kaydet);
        btn_kategori.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, KATEGORI_RESTFUL_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Kategori_Ekle_Activity.this,response.toString(),Toast.LENGTH_LONG).show();
                textView.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                                    params.put("kategori_adi",edt_kategori_ismi.getText().toString().trim());
                return params;
            }
        };

        BenimVolleyim.getmInstance(Kategori_Ekle_Activity.this).addToRequestque(stringRequest);

    }
}
