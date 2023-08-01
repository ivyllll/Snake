package com.example.snake;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class qiantai extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intent1 = new Intent(getApplication(), MainActivity.class);
        PendingIntent activity = PendingIntent.getActivity(getApplicationContext(), 2, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification build = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.head)
                .setContentTitle("inform")
                .setContentText("Snake is running......")
                .setContentIntent(activity)
                .setAutoCancel(true)
                .build();
        //开始前台服务
        startForeground(200, build);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
