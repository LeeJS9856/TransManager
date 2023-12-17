package org.techtown.transmanager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SharedPreferencesManager {
    private static final String PREFERENCES_NAME = "my_preferences";

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, context.MODE_PRIVATE);
    }

    public static void setLoginInfo(Context context, String vihiclenumber, String password) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("vihiclenumber", vihiclenumber);
        editor.putString("password", password);

        editor.apply();
    }
    public static Map<String, String> getLoginInfo(Context context) {
        SharedPreferences prefs = getPreferences(context);
        Map<String, String> LoginInfo = new HashMap<>();
        String vihiclenumber = prefs.getString("vihiclenumber", "");
        String password = prefs.getString("password", "");

        LoginInfo.put("vihiclenumber", vihiclenumber);
        LoginInfo.put("password", password);

        return  LoginInfo;
    }

    public static void clearPreferences(Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}
