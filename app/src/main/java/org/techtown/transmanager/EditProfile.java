package org.techtown.transmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProfile extends AppCompatActivity {

    EditText ed_username, ed_phonenumber, ed_old_password, ed_new_password, ed_password_check;
    ImageButton bt_back;
    android.widget.Button bt_edit;
    String db_username, db_phonenumber, db_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);


        //번들에서 값 받아오기
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String vihicle_number = bundle.getString("vihicle_number");


        //xml과 연결
        bt_back = findViewById(R.id.back);
        ed_username = findViewById(R.id.edittext_name);
        ed_phonenumber = findViewById(R.id.edittext_phone_number);
        ed_old_password = findViewById(R.id.edittext_old_password);
        ed_new_password = findViewById(R.id.edittext_new_password);
        ed_password_check = findViewById(R.id.edittext_password_check);

        bt_edit = findViewById(R.id.button_edit_profile);

        //차량번호를 서버에 전달하고 프로필 정보 받아오기
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {
                        db_username = jsonObject.getString("username");
                        db_phonenumber = jsonObject.getString("phonenumber");
                        db_password = jsonObject.getString("password");

                        //edittext에 기존 정보 입력하기
                        ed_username.setText(db_username);
                        ed_phonenumber.setText(db_phonenumber);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "정보 불러오기 실패", Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ProfileRequest profileRequest = new ProfileRequest(vihicle_number, responseListener);
        RequestQueue queue = Volley.newRequestQueue(EditProfile.this);
        queue.add(profileRequest);

        //뒤로가기 버튼 클릭 이벤트
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //수정 버튼 클릭 이벤트
        bt_edit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                int canEdit = 0;
                //edittext값 가져오기
                String username = ed_username.getText().toString();
                String phonenumber = ed_phonenumber.getText().toString();
                String old_password = ed_old_password.getText().toString();
                String new_password = ed_new_password.getText().toString();
                String password_check = ed_password_check.getText().toString();

                if(username.isEmpty()) {
                    printToast("이름을 입력해주세요.");
                }
                else if(phonenumber.isEmpty()) {
                    printToast("전화번호를 입력해주세요");
                }
                //비밀번호 입력이 없을 시 수정 가능
                else if(old_password.isEmpty()&&new_password.isEmpty()&&password_check.isEmpty()) {
                    canEdit = 1;
                }
                //기존 비밀번호가 일치하지 않을 떄
                else if(!old_password.equals(db_password)) {
                    printToast("기존 비밀번호가 일치하지 않습니다.");
                }
                else if(new_password.isEmpty()) {
                    printToast("새로운 비밀번호를 입력해주세요.");
                }
                else if(!new_password.equals(password_check)) {
                    printToast("비밀번호 재입력이 일치하지 않습니다.");
                }
                else {
                    canEdit = 2;
                }

                //데이터 베이스의 프로필 수정
                if(canEdit==1) { //비밀번호 없는 수정
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if(success) {
                                    printToast("수정 되었습니다.");
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    EditProfileNOPWRequest editProfileRequest = new EditProfileNOPWRequest(username, vihicle_number, phonenumber, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(EditProfile.this);
                    queue.add(editProfileRequest);
                }
                else if(canEdit==2) { //비밀번호 있는 수정
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if(success) {
                                    printToast("수정 되었습니다.");
                                    finish();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    EditProfilePWRequest editProfileRequest = new EditProfilePWRequest(username, vihicle_number, phonenumber, new_password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(EditProfile.this);
                    queue.add(editProfileRequest);
                }

            }
        });
    }

    public void printToast(String text) {
        Toast.makeText(EditProfile.this, text, Toast.LENGTH_SHORT).show();
    }
}