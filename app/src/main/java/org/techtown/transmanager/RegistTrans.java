package org.techtown.transmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;

public class RegistTrans extends AppCompatActivity {
    ImageButton bt_back;
    Spinner sp_from, sp_to, sp_product;
    EditText ed_quantity;
    android.widget.Button bt_regist_trans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_trans);


        //번들에서 값 받아오기
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String vihicle_number = bundle.getString("vihicle_number");
        String[] arr_From = bundle.getStringArray("From");
        String[] arr_To = bundle.getStringArray("To");
        String[] arr_Product = bundle.getStringArray("Product");

        //xml과 연결
        bt_back = findViewById(R.id.back);
        sp_from = findViewById(R.id.spinner_from);
        sp_to = findViewById(R.id.spinner_to);
        sp_product = findViewById(R.id.spinner_product);
        ed_quantity = findViewById(R.id.edittext_quantity);
        bt_regist_trans = findViewById(R.id.button_regist_trans);

        //뒤로가기 버튼
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        for(int i=0;i<arr_Product.length;i++) {
            printLog(arr_Product[i]+"");
        }

    }

    private void printLog(String text) {
        Log.d("RegistTrasn", text);
    }
}