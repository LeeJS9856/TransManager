package org.techtown.transmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RegistTrans extends AppCompatActivity {
    ImageButton bt_back;
    Spinner sp_from, sp_to, sp_product, sp_agency;
    EditText ed_quantity;
    android.widget.Button bt_regist_trans;
    String[] arr_From, arr_To, arr_Product, arr_Agency;
    ArrayList<String> list_from = new ArrayList<>();
    ArrayList<String> list_to = new ArrayList<>();
    ArrayList<String> list_product = new ArrayList<>();
    ArrayList<String> list_agency = new ArrayList<>();


    String choiced_from, choiced_to, choiced_product, entered_quantity, choiced_agency;
    private long mLastClickTime = 0; //중복 클릭 방지



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.regist_trans);


        //번들에서 값 받아오기
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String vihicle_number = bundle.getString("vihicle_number");

        String intent_product = bundle.getString("product");
        String intent_start = bundle.getString("start");
        String intent_end = bundle.getString("end");
        String intent_quantity = bundle.getString("quantity");
        String intent_agency = bundle.getString("agency");

        //xml과 연결
        bt_back = findViewById(R.id.back);
        sp_agency = findViewById(R.id.spinner_agency);
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

        ed_quantity.setText(intent_quantity);

        //서버에서 맵 데이터 가져오기
        Response.Listener<String> mapResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    int length = jsonArray.length();
                    for(int i=0;i<length;i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String property = item.getString("property");
                        String name = item.getString("name");
                        if(property.equals("출발지")) {
                            list_from.add(name);
                        }
                        else if(property.equals("도착지")) {
                            list_to.add(name);
                        }
                        else if(property.equals("제품")) {
                            list_product.add(name);
                        }
                        else if(property.equals("대리점")) {
                            list_agency.add(name);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        MapRequest mapRequest = new MapRequest(mapResponseListener);
        RequestQueue MapQueue = Volley.newRequestQueue(RegistTrans.this);
        MapQueue.add(mapRequest);

        //비동기적으로 작동하는 onresponse메서드를 위해 딜레이시켜서 배열 가져오기
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //리스트를 배열에 집어넣기
                arr_From = list_from.toArray(new String[list_from.size()]);
                arr_To = list_to.toArray(new String[list_to.size()]);
                arr_Product = list_product.toArray(new String[list_product.size()]);
                arr_Agency = list_agency.toArray(new String[list_agency.size()]);


                //스피너 적용하기
                ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(
                        RegistTrans.this, android.R.layout.simple_spinner_item, arr_From);
                ArrayAdapter<String> toAdapter = new ArrayAdapter<>(
                        RegistTrans.this, android.R.layout.simple_spinner_item, arr_To);
                ArrayAdapter<String> productAdapter = new ArrayAdapter<>(
                        RegistTrans.this, android.R.layout.simple_spinner_item, arr_Product);
                ArrayAdapter<String> agencyAdapter = new ArrayAdapter<>(
                        RegistTrans.this, android.R.layout.simple_spinner_item, arr_Agency);
                fromAdapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                toAdapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                productAdapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                agencyAdapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                sp_from.setAdapter(fromAdapter);
                sp_to.setAdapter(toAdapter);
                sp_product.setAdapter(productAdapter);
                sp_agency.setAdapter(agencyAdapter);

                //초기값 설정
                for(int i=0;i<arr_From.length;i++) {
                    if(arr_From[i].equals(intent_start)) {
                        sp_from.setSelection(i);
                    }
                }
                for(int i=0;i<arr_To.length;i++) {
                    if(arr_To[i].equals(intent_end)) {
                        sp_to.setSelection(i);
                    }
                }
                for(int i=0;i<arr_Product.length;i++) {
                    if(arr_Product[i].equals(intent_product)) {
                        sp_product.setSelection(i);
                    }
                }
                for(int i=0;i<arr_Agency.length;i++) {
                    if(arr_Agency[i].equals(intent_agency)) {
                        sp_agency.setSelection(i);
                    }
                }


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

                sp_agency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        choiced_agency = arr_Agency[i];
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

                //중복 클릭 방지
                if(SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
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
                                    choiced_product, choiced_from, choiced_to, entered_quantity, choiced_agency, responseListener);
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