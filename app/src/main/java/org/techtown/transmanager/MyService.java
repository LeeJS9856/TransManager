package org.techtown.transmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogRecord;

public class MyService extends Service {
    private final String DEFAULT = "DEFAULT";
    Context context = MyService.this;
    String vihiclenumber;
    int dispatchCount = 0;
    Thread thread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("service", "onCreate실행");
    }

    public void onDestroy() {
        Log.d("service", "onDestory실행");
        thread.start();
    }



    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                NotificationChannel notificationChannel = new NotificationChannel(DEFAULT, "default channel", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(notificationChannel);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, DEFAULT)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSmallIcon(R.drawable.icon_noti_img)
                        .setContentTitle("배차요청")
                        .setContentText("새로운 배차 요청이 수신되었습니다.")
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

                Intent intent = new Intent(context, Dispatch.class);
                Bundle bundle = new Bundle();
                bundle.putString("vihiclenumber", vihiclenumber);
                intent.putExtras(bundle);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                builder.setContentIntent(pendingIntent);
                manager.notify(1, builder.build());
            }

        }

        ;
    };
    protected boolean myRunning;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service", "onStartCommand실행");
        if (intent == null) {
            return Service.START_STICKY;
        } else {
            vihiclenumber = intent.getStringExtra("vihiclenumber");
            thread = new ServiceTread(mHandler, intent);
            thread.start();
        }


        return Service.START_STICKY;
    }


    protected void getDispatchData() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] getTime = sdf.format(date).split("-");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    int length = jsonArray.length();
                    int count = 0;
                    for (int i = 0; i < length; i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String day = item.getString("day");
                        if (getTime[2].equals(day)) count++;
                    }
                    if (count != 0 && length != dispatchCount) {
                        mHandler.sendEmptyMessage(0);
                        dispatchCount = length;
                    } else {
                        Toast.makeText(context, "running service", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RequestDispatchVihicleData requestDispatchVihicleData = new RequestDispatchVihicleData(getTime[0], getTime[1], vihiclenumber, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(requestDispatchVihicleData);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("Error", "onTaskRemoved - 강제종료" + rootIntent);
        Toast.makeText(context, "onTaskRemoved", Toast.LENGTH_SHORT).show();
        stopSelf();
    }

    public class ServiceTread extends Thread {
        Handler handler;
        Intent intent;

        public ServiceTread(Handler handler, Intent intent) {
            this.handler = handler;
            this.intent = intent;
        }

        @Override
        public void run() {
            while (true) {
                getDispatchData();
                try {
                    Thread.sleep(1000);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE);

                } catch (Exception e) {
                }
            }
        }
    }
}

