package org.techtown.transmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditTrans extends AppCompatActivity {

    Spinner sp_from, sp_to, sp_product;
    EditText ed_quantity;
    android.widget.Button bt_edit;
    ImageButton bt_back;
    String[] arr_From, arr_To, arr_Product;

    String choiced_from, choiced_to, choiced_product, entered_quantity, vihiclenumber;
    private long mLastClickTime = 0; //중복 클릭 방지
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_trans);;

        //번들에서 값 받아오기
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int db_id = bundle.getInt("id");
        String db_year = bundle.getString("year");
        String db_month = bundle.getString("month");
        String db_day = bundle.getString("day");
        String db_vihiclenumber = bundle.getString("vihiclenumber");
        String db_product = bundle.getString("product");
        String db_start = bundle.getString("start");
        String db_end = bundle.getString("end");
        String db_quantity = bundle.getString("quantity");
        vihiclenumber = db_vihiclenumber;

        //xml과 연결하기
        sp_from = findViewById(R.id.spinner_from);
        sp_to = findViewById(R.id.spinner_to);
        sp_product = findViewById(R.id.spinner_product);

        ed_quantity = findViewById(R.id.edittext_quantity);

        bt_edit = findViewById(R.id.button_edit_trans);
        bt_back = findViewById(R.id.back);

        //뒤로가기 버튼 클릭 이벤트
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditTrans.this, ListTrans.class);
                intent.putExtra("vihicle_number", vihiclenumber);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        //수량 초기값 설정
        ed_quantity.setText(db_quantity);

        //출발지 데이터 가져와서 배열에 집어넣기
        Response.Listener<String> fromResponseListener = new Response.Listener<String>() {
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                        int length = jsonResponse.length()-1;
                        arr_From = new String[length];
                        for(int i=0;i<length;i++) {
                            String db = jsonResponse.getString(String.valueOf(i));
                            arr_From[i] = db;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        FromRequest  fromRequest = new FromRequest(fromResponseListener);
        RequestQueue FromQueue = Volley.newRequestQueue(EditTrans.this);
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
                        arr_To = new String[length];
                        for(int i=0;i<length;i++) {
                            String db = jsonResponse.getString(String.valueOf(i));
                            arr_To[i] = db;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ToRequest toRequest = new ToRequest(toResponseListener);
        RequestQueue ToQueue = Volley.newRequestQueue(EditTrans.this);
        ToQueue.add(toRequest);

        //제품 데이터 가져와서 배열에 집어넣기
        Response.Listener<String> productResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                        int length = jsonResponse.length()-1;
                        arr_Product = new String[length];
                        for(int i=0;i<length;i++) {
                            String db = jsonResponse.getString(String.valueOf(i));
                            arr_Product[i] = db;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ProductRequest productRequest = new ProductRequest(productResponseListener);
        RequestQueue ProductQueue = Volley.newRequestQueue(EditTrans.this);
        ProductQueue.add(productRequest);


        //비동기적으로 작동하는 onresponse메서드를 위해 딜레이시켜서 배열 가져오기
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //스피너 적용하기
                ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(
                        EditTrans.this, android.R.layout.simple_spinner_item, arr_From);
                ArrayAdapter<String> toAdapter = new ArrayAdapter<>(
                        EditTrans.this, android.R.layout.simple_spinner_item, arr_To);
                ArrayAdapter<String> productAdapter = new ArrayAdapter<>(
                        EditTrans.this, android.R.layout.simple_spinner_item, arr_Product);
                fromAdapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                toAdapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                productAdapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                sp_from.setAdapter(fromAdapter);
                sp_to.setAdapter(toAdapter);
                sp_product.setAdapter(productAdapter);

                //초기값 설정
                for(int i=0;i<arr_From.length;i++) {
                    if(arr_From[i].equals(db_start)) {
                        sp_from.setSelection(i);
                    }
                }
                for(int i=0;i<arr_To.length;i++) {
                    if(arr_To[i].equals(db_end)) {
                        sp_to.setSelection(i);
                    }
                }
                for(int i=0;i<arr_Product.length;i++) {
                    if(arr_Product[i].equals(db_product)) {
                        sp_product.setSelection(i);
                    }
                }

                sp_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        choiced_from = arr_From[i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                sp_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        choiced_to = arr_To[i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                sp_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        choiced_product = arr_Product[i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }, 1000);

        //수정버튼 이벤트 처리하기
        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //중복 클릭 방지
                if(SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                //수량 입력받기
                entered_quantity = ed_quantity.getText().toString();

                if(entered_quantity.isEmpty()) {
                    printToast("수량을 입력해주세요");
                }
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Intent intent = new Intent(EditTrans.this, ListTrans.class);
                            intent.putExtra("vihicle_number", db_vihiclenumber);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    };

                    String id = Integer.toString(db_id);
                    EditTransUpdateRequest editTransUpdateRequest =
                            new EditTransUpdateRequest(id, db_year, db_month, db_day, db_vihiclenumber,
                                    choiced_product, choiced_from, choiced_to, entered_quantity, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(EditTrans.this);
                    queue.add(editTransUpdateRequest);
                }
            }
        });


    }

    public void onBackPressed() { //뒤로가기 누를 시 이전페이지로
        Intent intent = new Intent(EditTrans.this, ListTrans.class);
        intent.putExtra("vihicle_number", vihiclenumber);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void printToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
