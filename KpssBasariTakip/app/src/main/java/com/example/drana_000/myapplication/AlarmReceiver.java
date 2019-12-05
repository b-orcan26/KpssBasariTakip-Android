package com.example.drana_000.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Drana_000 on 25.1.2018.
 *
 * Creater da alarm manager daki tüm alarmlar silindi tekrar olşturuldu bu class 'ın görevi alarm tetiklenince onu dinleyip
 * notification basmaktır.
 * intent e konuyu koydum burda alarm saatinde tetiklenen intent in içinde ki konuyu alıp notification basalşım
 *
 *
 *
 *
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent ıntent) {

        Log.e("run","alarm receiver çalıştı");

        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);


        String konu=ıntent.getExtras().getString("konu");
        int notificationID=preferences.getInt("notificationID",0);

        NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder=new Notification.Builder(context);
        builder.setContentTitle(" 5 dk sonra çalışma zamanı ");
        builder.setContentText(konu);
        builder.setSmallIcon(R.drawable.happy_icon);
        builder.setTicker(" 5 dk sonra Çalışma zamanı ");
        builder.setAutoCancel(false);

        Intent intent1=new Intent(context,AlarmReceiver.class);
        PendingIntent pi=PendingIntent.getActivity(context,notificationID,intent1,0);
        builder.setContentIntent(pi);

        Notification not=builder.getNotification();
        not.vibrate = new long[]{500,500,500,500};
        manager.notify(notificationID,not);



        notificationID+=1;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("notificationID",notificationID);
        editor.apply();

    }
}
