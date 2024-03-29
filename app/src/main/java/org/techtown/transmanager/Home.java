package org.techtown.transmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.service.controls.ControlsProviderService;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Home extends AppCompatActivity {

    TextView text_vihicle_number;
    android.widget.Button regist_trans, list_trans, edit_profile, logout, dispatch;
    private long backKeyPressedTime = 0;

    Context context = Home.this;
    String vihiclenumber;
    final int PERMISSION_REQUEST_CODE = 112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        setNotiPermission();
        startService();

        //번들에서 값 받아오기
        Intent intent_bundle = getIntent();
        Bundle bundle = intent_bundle.getExtras();
        vihiclenumber = bundle.getString("vihicle_number");

        //텍스트뷰, 버튼 정의
        text_vihicle_number = findViewById(R.id.text_trans_number);
        regist_trans = findViewById(R.id.button_regist_trans);
        list_trans = findViewById(R.id.button_list_trans);
        edit_profile = findViewById(R.id.button_edit_profile);
        logout = findViewById(R.id.button_logout);
        dispatch = findViewById(R.id.button_received_request);



        //상단 차량번호 바꾸기
        text_vihicle_number.setText(vihiclenumber);

        //운송등록 클릭 이벤트
        regist_trans.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, RegistTrans.class);
            intent.putExtra("vihicle_number", vihiclenumber);
            startActivity(intent);
        });

        //운송내역 클릭 이벤트
        list_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, ListTrans.class);
                intent.putExtra("vihicle_number", vihiclenumber);
                startActivity(intent);
            }
        });

        //프로필 수정 클릭 이벤트
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, EditProfile.class);
                intent.putExtra("vihicle_number", vihiclenumber);
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
                SharedPreferencesManager.clearPreferences(Home.this); //자동로그인 해제
                Toast.makeText(Home.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = new Intent(getApplicationContext(), MyService.class);
        intent.putExtra("vihiclenumber", vihiclenumber);
        startService(intent);


        dispatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Dispatch.class);
                intent.putExtra("vihiclenumber", vihiclenumber);
                startActivity(intent);
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
    private void setNotiPermission() {
        if(Build.VERSION.SDK_INT > 32) {
            if(!shouldShowRequestPermissionRationale("112")) {
                getNotificationPermission();
            }
        }
    }

    private void getNotificationPermission() {
        try {
            if(Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
            }
        }catch (Exception e){}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else {

                }
                return;
        }
    }

    public void startService() {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("vihiclenumber", vihiclenumber);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }
    }

}