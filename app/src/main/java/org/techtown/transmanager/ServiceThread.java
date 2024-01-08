package org.techtown.transmanager;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;

public class ServiceThread extends Thread{
    Handler handler;
    boolean isRun =true;

    public ServiceThread(Handler handler) {
        this.handler = handler;
    }

    public void stopForever() {
        synchronized(this) {
            this.isRun = false;
        }
    }

    public void run() {
        while(isRun) {
            handler.
        }
        try{
            Thread.sleep(10000);
        }catch (Exception e) {};
    }
}
