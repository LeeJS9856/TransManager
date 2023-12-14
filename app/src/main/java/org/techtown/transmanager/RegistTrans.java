package org.techtown.transmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RegistTrans extends AppCompatActivity {
    ImageButton bt_back;
    Spinner sp_from, sp_to, sp_product;
    EditText ed_quantity;
    android.widget.Button bt_regist_trans;
    String[] arr_From, arr_To, arr_Product;

    String choiced_from, choiced_to, choiced_product, entered_quantity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.regist_trans);


        //번들에서 값 받아오기
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String vihicle_number = bundle.getString("vihicle_number");

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

        //출발지 데이터 가져와서 배열에 집어넣기
        Response.Listener<String> fromResponseListener = new Response.Listener<String>() {
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                        int length = jsonResponse.length()-1;
                        arr_From = new String[length];
                        for(int i=0;i<length;i++) {
                            String db = jsonResponse.getString(String.valueOf(i));
                            arr_From[i] = db;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        FromRequest  fromRequest = new FromRequest(fromResponseListener);
        RequestQueue FromQueue = Volley.newRequestQueue(RegistTrans.this);
        FromQueue.add(fromRequest);

        //도착지 데이터 가져와서 배열에 집어넣기
        Response.Listener<String> toResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                        int length = jsonResponse.length()-1;
                        arr_To = new String[length];
                        for(int i=0;i<length;i++) {
                            String db = jsonResponse.getString(String.valueOf(i));
                            arr_To[i] = db;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ToRequest toRequest = new ToRequest(toResponseListener);
        RequestQueue ToQueue = Volley.newRequestQueue(RegistTrans.this);
        ToQueue.add(toRequest);

        //제품 데이터 가져와서 배열에 집어넣기
        Response.Listener<String> productResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                        int length = jsonResponse.length()-1;
                        arr_Product = new String[length];
                        for(int i=0;i<length;i++) {
                            String db = jsonResponse.getString(String.valueOf(i));
                            arr_Product[i] = db;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ProductRequest productRequest = new ProductRequest(productResponseListener);
        RequestQueue ProductQueue = Volley.newRequestQueue(RegistTrans.this);
        ProductQueue.add(productRequest);


        //비동기적으로 작동하는 onresponse메서드를 위해 딜레이시켜서 배열 가져오기
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //스피너 적용하기
                ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(
                        RegistTrans.this, android.R.layout.simple_spinner_item, arr_From);
                ArrayAdapter<String> toAdapter = new ArrayAdapter<>(
                        RegistTrans.this, android.R.layout.simple_spinner_item, arr_To);
                ArrayAdapter<String> productAdapter = new ArrayAdapter<>(
                        RegistTrans.this, android.R.layout.simple_spinner_item, arr_Product);
                fromAdapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                toAdapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                productAdapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                sp_from.setAdapter(fromAdapter);
                sp_to.setAdapter(toAdapter);
                sp_product.setAdapter(productAdapter);


                sp_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        choiced_from = arr_From[i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                sp_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        choiced_to = arr_To[i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                sp_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        choiced_product = arr_Product[i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }, 1000);



        //등록버튼 이벤트 처리하기
        bt_regist_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //오늘날짜 구하기
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String[] getTime = sdf.format(date).split("-");

                //수량 입력받기
                entered_quantity = ed_quantity.getText().toString();

                if(entered_quantity.isEmpty()) {
                    printToast("수량을 입력해주세요");
                }
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if(success) {
                                    printToast("등록되었습니다.");
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    RegistTransRequest registTransRequestRequest =
                            new RegistTransRequest(getTime[0], getTime[1], getTime[2], vihicle_number,
                                    choiced_product, choiced_from, choiced_to, entered_quantity, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegistTrans.this);
                    queue.add(registTransRequestRequest);
                }
            }
        });





    }


    private void printLog(String text) {
        Log.d("RegistTrasn", text);
    }
    private void printToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}