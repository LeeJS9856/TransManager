package org.techtown.transmanager;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EditProfilePWRequest extends StringRequest {

    final static private String URL = "http://gm8668.dothome.co.kr/editprofile_pw.php";
    private Map<String, String> map;

    public EditProfilePWRequest(String username, String vihiclenumber, String phonenumber, String password, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("username", username);
        map.put("vihiclenumber", vihiclenumber);
        map.put("phonenumber", phonenumber);
        map.put("password", password);
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
