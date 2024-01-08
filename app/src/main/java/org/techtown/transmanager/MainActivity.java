package org.techtown.transmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String vihiclenumber, password;
    Context context = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auto_login();

    }

    protected void auto_login() {
        Map<String, String> LoginInfo = SharedPreferencesManager.getLoginInfo(this);
        if(!LoginInfo.isEmpty()) {
            vihiclenumber = LoginInfo.get("vihiclenumber");
            password = LoginInfo.get("password");

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if(success) {
                            String db_vihicle_number = jsonObject.getString("vihiclenumber");
                            String password = jsonObject.getString("password");

                            Intent intent = new Intent(context, Home.class);
                            intent.putExtra("vihicle_number", db_vihicle_number);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "자동로그인 되었습니다", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent intent = new Intent(context, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            LoginRequest loginRequest = new LoginRequest(vihiclenumber, password, responseListener);
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(loginRequest);

        }
        else {
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }



}
