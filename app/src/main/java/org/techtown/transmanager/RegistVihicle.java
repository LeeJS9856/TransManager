
package org.techtown.transmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegistVihicle extends AppCompatActivity {
    ImageButton back;
    EditText name, vihicle_number, phone_number, password, password_check;
    android.widget.Button regist_request;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_vihicle);

        //뒤로가기 버튼
        back = findViewById(R.id.back);
        back.setOnClickListener(v->onBackPressed() );

        //기입 항목
        name = findViewById(R.id.edittext_name);
        vihicle_number = findViewById(R.id.edittext_vihivle_number);
        phone_number = findViewById(R.id.edittext_phone_number);
        password = findViewById(R.id.edittext_password);
        password_check = findViewById(R.id.edittext_password_check);

        //등록 요청 버튼
        regist_request = findViewById(R.id.button_regist_request);
        regist_request.setOnClickListener(v-> {
            if(name.getText().toString().isEmpty()) {
                printToast("이름을 입력해주세요.");
            }
            else if(vihicle_number.getText().toString().isEmpty()) {
                printToast("차량번호를 입력해주세요.");
            }
            else if(phone_number.getText().toString().isEmpty()) {
                printToast("핸드폰 번호를 입력해주세요.");
            }
            else if(password.getText().toString().isEmpty()) {
                printToast("비밀번호를 입력해주세요.");
            }
            else if(password.getText().toString().equals(password_check.getText().toString())) {
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                printToast("등록 요청 되었습니다.");
            }
            else {
                printToast("비밀번호가 일치하지 않습니다.");
            }

        });
    }

    private void printToast(String text) {
        Toast.makeText(RegistVihicle.this, text, Toast.LENGTH_LONG).show();
    }
}