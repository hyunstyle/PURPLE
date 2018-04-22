package com.hyunstyle.inhapet.thread;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hyunstyle.inhapet.MainActivity;
import com.hyunstyle.inhapet.R;

/**
 * Created by sh on 2018-03-08.
 */

public class NotificationHandler extends Handler {

    private Context context, applicationContext;
    private NotificationManager notificationManager;

    public NotificationHandler(Context context, NotificationManager notificationManager, Context applicationContext) {
        this.context = context;
        this.applicationContext = applicationContext;
        this.notificationManager = notificationManager;
    }

    @Override
    public void handleMessage(Message msg) {

        Log.d("handleme", "handlemessage");
        Intent intent = new Intent(this.context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(applicationContext)
                                    .setContentTitle("알림 타이틀")
                                    .setContentText("TEXT")
                                    .setSmallIcon(R.drawable.cat)
                                    .setTicker("알림도착")
                                    .setContentIntent(pendingIntent)
                                    .setCategory(Notification.CATEGORY_MESSAGE)
                                    .setPriority(Notification.PRIORITY_HIGH)
                                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                                    .build();

        notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        notification.flags = Notification.FLAG_AUTO_CANCEL;


        notificationManager.notify(888, notification);
        // pending intent : A(this,context)한테, B 인텐트(inent)를 특정시점에 실행하라고 해줘.(Notification 등)


    }
}
