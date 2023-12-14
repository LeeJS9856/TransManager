package org.techtown.transmanager;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegistTransRequest extends StringRequest {

    final static private String URL = "http://gm8668.dothome.co.kr/registTrans.php";
    private Map<String, String> map;

    public RegistTransRequest(String year, String month, String day, String vihiclenumber
            ,String product, String start, String end, String quantity, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("year", year);
        map.put("month", month);
        map.put("day", day);
        map.put("vihiclenumber", vihiclenumber);
        map.put("product", product);
        map.put("start", start);
        map.put("end", end);
        map.put("quantity", quantity);

    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}