package org.techtown.transmanager;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MapRequest extends StringRequest {

    final static private String URL = "http://gm8668.dothome.co.kr/requestMap.php";
    private Map<String, String> map;

    public MapRequest(Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
