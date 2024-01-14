package org.techtown.transmanager;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateDispatchData extends StringRequest {

    final static private String URL = "http://gm8668.dothome.co.kr/updateDispatchData.php";
    private Map<String, String> map;

    public UpdateDispatchData(String vihiclenumber, String conf, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("vihiclenumber", vihiclenumber);
        map.put("conf", conf);
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
