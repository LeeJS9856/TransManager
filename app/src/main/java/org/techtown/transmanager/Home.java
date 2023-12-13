package org.techtown.transmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity {

    TextView text_vihicle_number;
    android.widget.Button regist_trans, list_trans, edit_profile, logout;
    private long backKeyPressedTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        //번들에서 값 받아오기
        Intent intent_bundle = getIntent();
        Bundle bundle = intent_bundle.getExtras();
        String vihicle_number = bundle.getString("vihicle_number");

        //텍스트뷰, 버튼 정의
        text_vihicle_number = findViewById(R.id.text_trans_number);
        regist_trans = findViewById(R.id.button_regist_trans);
        list_trans = findViewById(R.id.button_list_trans);
        edit_profile = findViewById(R.id.button_edit_profile);
        logout = findViewById(R.id.button_logout);



        //상단 차량번호 바꾸기
        text_vihicle_number.setText(vihicle_number);

        //운송등록 클릭 이벤트
        regist_trans.setOnClickListener(v -> {

            //제품 데이터 가져와서 배열에 집어넣기
            Response.Listener<String> productResponseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success) {
                            int length = jsonResponse.length()-1;
                            String[] arr = new String[length];
                            for(int i=0;i<length;i++) {
                                String db = jsonResponse.getString(String.valueOf(i));
                                arr[i] = db;
                            }
                            Intent intent = new Intent(Home.this, RegistTrans.class);
                            intent.putExtra("Product", arr);
                            intent.putExtra("vihicle_number", vihicle_number);
                            startActivity(intent);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            ProductRequest productRequest = new ProductRequest(productResponseListener);
            RequestQueue ProductQueue = Volley.newRequestQueue(Home.this);
            ProductQueue.add(productRequest);
        });

        //운송내역 클릭 이벤트
        list_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, ListTrans.class);
                intent.putExtra("vihicle_number", vihicle_number);

            }
        });

        //프로필 수정 클릭 이벤트
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, EditProfile.class);
                intent.putExtra("vihicle_number", vihicle_number);
                startActivity(intent);
            }
        });
        //로그아웃 클릭 이벤트
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(Home.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onBackPressed() { //뒤로가기 두번 누를시 앱 종료
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            finish();
        }


    }

    public void giveIntent() { //RegistTrans에 줄 Intent 수정
        //출발지 데이터 가져와서 배열에 집어넣기
        Response.Listener<String> fromResponseListener = new Response.Listener<String>() {
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                        int length = jsonResponse.length()-1;
                        String[] arr = new String[length];
                        for(int i=0;i<=length;i++) {
                            String db = jsonResponse.getString(String.valueOf(i));
                            arr[i] = db;
                        }
                        Intent intent = new Intent(Home.this, RegistTrans.class);
                        intent.putExtra("From", arr);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        FromRequest  fromRequest = new FromRequest(fromResponseListener);
        RequestQueue FromQueue = Volley.newRequestQueue(Home.this);
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
                        String[] arr = new String[length];
                        for(int i=0;i<=length;i++) {
                            String db = jsonResponse.getString(String.valueOf(i));
                            arr[i] = db;
                        }
                        Intent intent = new Intent(Home.this, RegistTrans.class);
                        intent.putExtra("To", arr);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ToRequest toRequest = new ToRequest(toResponseListener);
        RequestQueue ToQueue = Volley.newRequestQueue(Home.this);
        ToQueue.add(toRequest);


    }
}