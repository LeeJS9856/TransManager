package org.techtown.transmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Dispatch extends AppCompatActivity {
    ImageButton bt_back;
    RecyclerView recyclerView;
    String vihicle_number;
    Context context = Dispatch.this;
    static ArrayList<TransData> data = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dispatch);

        //인텐트값 받아오기
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            vihicle_number = bundle.getString("vihiclenumber");
            Log.d("dispatch", "vihiclenumber is "+ vihicle_number);
        }
        xml();
        back();
        getDispatchData();
        setRecyclerView();
    }

    protected void xml() {
        bt_back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recyclerView);
    }

    protected void back() {
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Home.class);
                intent.putExtra("vihicle_number", vihicle_number);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
    private void setRecyclerView() {
        DispatchAdapter dispatchAdapter = new DispatchAdapter(context);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dispatchAdapter.submitData(getData());
                recyclerView.setAdapter(dispatchAdapter);
                recyclerView.scrollToPosition(data.size()-1);
            }
        }, 1000);
    }

    private ArrayList<TransData> getData() {
        return data;
    }
    private void getDispatchData() {
        //오늘날짜 구하기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] today = sdf.format(date).split("-");

        data.clear();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    int length = jsonArray.length();
                    for(int i=0;i<length;i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String day = item.getString("day");
                        String product = item.getString("product");
                        String start = item.getString("start");
                        String end = item.getString("end");
                        String quantity = item.getString("quantity");
                        String agency = item.getString("agency");


                        if(today[2].equals(day)){
                            TransData transData = new TransData(0, today[0], today[1], day, vihicle_number, product, start, end, quantity, agency);
                            data.add(transData);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RequestDispatchVihicleData requestDispatchVihicleData = new RequestDispatchVihicleData(today[0], today[1], vihicle_number, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(requestDispatchVihicleData);
    }
}
