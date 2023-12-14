package org.techtown.transmanager;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DataRequest extends StringRequest {

    final static private String URL = "http://gm8668.dothome.co.kr/requestData.php";
    private Map<String, String> map;

    public DataRequest(String year, String month, String vihiclenumber, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("year", year);
        map.put("month", month);
        map.put("vihiclenumber", vihiclenumber);
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
