package org.techtown.transmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    android.widget.Button regist_vihicle, login;
    EditText ed_vihicle_number, ed_password;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.login);
        //ID, PW
        ed_vihicle_number = findViewById(R.id.edittext_vihivle_number);
        ed_password = findViewById(R.id.edittext_password);


        //차량 등록 버튼
        regist_vihicle = findViewById(R.id.button_regist_vihicle);

        //차량등록 버튼 클릭시 차량등록 페이지로 이동
        regist_vihicle.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistVihicle.class);
            startActivity(intent);
        });

        //로그인 버튼
        login = findViewById(R.id.button_login);

        //로그인 버튼 클릭 이벤트
        login.setOnClickListener(v -> {
            final String vihicle_number = ed_vihicle_number.getText().toString();
            String password = ed_password.getText().toString();

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if(success) {
                            String vihicle_number = jsonObject.getString("vihiclenumber");
                            String password = jsonObject.getString("password");
                            Intent intent = new Intent(Login.this, Home.class);
                            intent.putExtra("vihicle_number", vihicle_number);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "차량번호 혹은 비밀번호가 않습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            LoginRequest loginRequest = new LoginRequest(vihicle_number, password, responseListener);
            RequestQueue queue = Volley.newRequestQueue(Login.this);
            queue.add(loginRequest);
        });
    }
}

