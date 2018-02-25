package com.tseluikoartem.ening.yandexmobdevproject.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.tseluikoartem.ening.yandexmobdevproject.R;
import com.yandex.metrica.push.YandexMetricaPush;

import static android.content.Context.NOTIFICATION_SERVICE;

public class SilentPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Извлечение данных из вашего push-уведомления
        String payload = intent.getStringExtra(YandexMetricaPush.EXTRA_PAYLOAD);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.logo_gradient)
                        .setContentTitle("За вами следят")
                        .setContentText("Но это не точно.");

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}