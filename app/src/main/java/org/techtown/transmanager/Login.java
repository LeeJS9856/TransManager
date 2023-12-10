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

public class Login extends AppCompatActivity {
    android.widget.Button regist_vihicle;
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.login);

        //차량 등록 버튼
        regist_vihicle = findViewById(R.id.button_regist_vihicle);

        //차량등록 버튼 클릭시 차량등록 페이지로 이동
        regist_vihicle.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistVihicle.class);
            startActivity(intent);
        });
    }
}

