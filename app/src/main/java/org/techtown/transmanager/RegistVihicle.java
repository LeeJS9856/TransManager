
package org.techtown.transmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistVihicle extends AppCompatActivity {
    private ImageButton back;
    private EditText ed_name, ed_vihicle_number, ed_phone_number, ed_password, ed_password_check;
    private android.widget.Button regist_request;
    private boolean validate = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_vihicle);



        //뒤로가기 버튼
        back = findViewById(R.id.back);
        back.setOnClickListener(v->onBackPressed() );

        //기입 항목
        ed_name = findViewById(R.id.edittext_name);
        ed_vihicle_number = findViewById(R.id.edittext_vihivle_number);
        ed_phone_number = findViewById(R.id.edittext_phone_number);
        ed_password = findViewById(R.id.edittext_password);
        ed_password_check = findViewById(R.id.edittext_password_check);

        //등록 요청 버튼
        regist_request = findViewById(R.id.button_regist_request);
        regist_request.setOnClickListener(v-> {

            String name = ed_name.getText().toString();
            String vihicle_number = ed_vihicle_number.getText().toString();
            String phone_number = ed_phone_number.getText().toString();
            String password = ed_password.getText().toString();
            String password_check = ed_password_check.getText().toString();

            //차량번호 중복 확인
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(!success) {
                            printToast("이미 존재하는 차량번호입니다.");
                        }else {
                            validate = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            ValidateRequest validateRequest = new ValidateRequest(vihicle_number, responseListener);
            RequestQueue queue = Volley.newRequestQueue(RegistVihicle.this);
            queue.add(validateRequest);

            if(name.isEmpty()) { //이름 입력이 안 되었을 때
                printToast("이름을 입력해주세요.");
            }
            else if(vihicle_number.isEmpty()) { //차량번호 입력이 안 되었을 때
                printToast("차량번호를 입력해주세요.");
            }
            else if(phone_number.isEmpty()) { //핸드폰 번호 입력이 안 되었을 때
                printToast("핸드폰 번호를 입력해주세요.");
            }
            else if(password.isEmpty()) {//비밀번호 입력이 안 되었을 때
                printToast("비밀번호를 입력해주세요.");
            }
            else if(password.equals(password_check)) { //등록요청이 가능할 때
                Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success && validate) {
                                Intent intent = new Intent(RegistVihicle.this, Login.class);
                                startActivity(intent);
                                printToast("등록 요청 되었습니다.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                RegistRequest registRequest = new RegistRequest(name, vihicle_number, phone_number, password, responseListener2);
                RequestQueue queue2 = Volley.newRequestQueue(RegistVihicle.this);
                queue2.add(registRequest);
            }
            else {
                printToast("비밀번호가 일치하지 않습니다."); //비밀번호 확인이 비밀번호와 다를 때
            }
        });
    }




    private void printToast(String text) {
        Toast.makeText(RegistVihicle.this, text, Toast.LENGTH_LONG).show();
    }

}