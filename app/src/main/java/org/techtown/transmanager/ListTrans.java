package org.techtown.transmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

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

public class ListTrans extends AppCompatActivity {
    Spinner spinner_year, spinner_month;
    String[] today, arr_year;

    String[] arr_month = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    String vihicle_number, choiced_year, choiced_month;
    ImageButton bt_back;
    RecyclerView recyclerView;
    static ArrayList<TransData> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_trans);

        //번들에서 값 받아오기
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        vihicle_number = bundle.getString("vihicle_number");

        //뒤로가기 버튼
        bt_back = findViewById(R.id.back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListTrans.this, Home.class);
                intent.putExtra("vihicle_number", vihicle_number);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        //오늘날짜 구하기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        today = sdf.format(date).split("-");
        choiced_year = today[0];
        choiced_month = today[1];

        //연도 배열 만들기
        int n = Integer.parseInt(today[0]) - 2023;
        arr_year = new String[n+1];
        for(int i=0;i<n+1;i++) {
            int m = 2023 + i;
            arr_year[i] = Integer.toString(m); //2023년~현재 연도 배열에 집어넣기
        }

        //리사이클러뷰 정의하기
        recyclerView = findViewById(R.id.recyclerView);
        TransDataAdapter transDataAdapter = new TransDataAdapter(ListTrans.this);

        //스피너 처리하기
        spinner_year = findViewById(R.id.spinner_year);
        spinner_month = findViewById(R.id.spinner_month);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(
                ListTrans.this, android.R.layout.simple_spinner_item, arr_year);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(
                ListTrans.this, android.R.layout.simple_spinner_item, arr_month);

        yearAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        monthAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);



        spinner_year.setAdapter(yearAdapter);
        spinner_month.setAdapter(monthAdapter);
        spinner_year.setSelection(Integer.parseInt(today[0])-2023);
        spinner_month.setSelection(Integer.parseInt(today[1])-1); //오늘 날짜로 초기값 지정

        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                choiced_year = arr_year[i];
                data.clear();
                //맨 처음 데이터가 두번 불러와지는 오류 수정
                if(System.currentTimeMillis() - now > 1000) getTransData(choiced_year, choiced_month);
                //비동기적으로 작동하는 onresponse메서드를 위해 딜레이시켜서 리사이클러뷰 실행
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        transDataAdapter.submitData(getData());
                        recyclerView.setAdapter(transDataAdapter);
                        recyclerView.scrollToPosition(data.size()-1);
                    }
                }, 1000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                choiced_month = arr_month[i];
                data.clear();
                getTransData(choiced_year, choiced_month);

                //비동기적으로 작동하는 onresponse메서드를 위해 딜레이시켜서 리사이클러뷰 실행
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        transDataAdapter.submitData(getData());
                        recyclerView.setAdapter(transDataAdapter);
                        recyclerView.scrollToPosition(data.size()-1);

                    }
                }, 1000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    private ArrayList<TransData> getData() {
        return data;
    }


    //서버에서 데이터 가져오기
    private void getTransData(String year, String month) {
        Response.Listener<String> dataResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    int length = jsonArray.length();
                    for(int i=0;i<length;i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        int id = item.getInt("id");
                        String day = item.getString("day");
                        String product = item.getString("product");
                        String start = item.getString("start");
                        String end = item.getString("end");
                        String quantity = item.getString("quantity");
                        String agency = item.getString("agency");

                        TransData transData = new TransData(id, year, month, day, vihicle_number, product, start, end, quantity, agency);
                        data.add(transData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        DataRequest dataRequest = new DataRequest(year, month, vihicle_number, dataResponseListener);
        RequestQueue DataQueue = Volley.newRequestQueue(ListTrans.this);
        DataQueue.add(dataRequest);
    }

    public void onBackPressed() { //뒤로가기 누를 시 이전페이지로
        Intent intent = new Intent(ListTrans.this, Home.class);
        intent.putExtra("vihicle_number", vihicle_number);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}