package org.techtown.transmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

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
    ServiceThread thread;
    private final String DEFAULT = "DEFAULT";
    Context context = MyService.this;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        return START_STICKY;
    }

    public void onDestroy() {
        thread.stopForever();
        thread = null;
    }

    class myServiceHandler extends Handler{
        @Override
        public void handleMessage(android.os.Message msg) {
            getDispatchData();
        }
    }
    protected void getDispatchData() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] getTime = sdf.format(date).split("-");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    int length = jsonArray.length();
                    for(int i=0;i<length;i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String day = item.getString("day");
                        String product = item.getString("product");
                        String start = item.getString("start");
                        String end = item.getString("end");
                        String quantity = item.getString("quantity");
                        String agency = item.getString("agency");
                    }

                    if(length!=0) {
                        setNotification();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RequestDispatchVihicleData requestDispatchVihicleData = new RequestDispatchVihicleData(getTime[0], getTime[1], vihiclenumber, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(requestDispatchVihicleData);
    }

    protected void setNotification() {
        createNotificationChannel(DEFAULT, "default channel", NotificationManager.IMPORTANCE_HIGH);
        Intent intent = new Intent(context, Dispatch.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        createNotification(DEFAULT, 1, "배차 요청", "새로운 배차 요청이 수신되었습니다.", intent);
    }

    void createNotificationChannel(String channelId, String ChannelName, int importance)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, ChannelName, importance));
        }
    }

    void createNotification(String channelId, int id, String title, String text, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.icon_img)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }

    void destroyNotification(int id)
    {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
}
