package com.example.drana_000.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;



public class AlarmCreater extends BroadcastReceiver {

    private SharedPreferences preferences;

    @Override

    public void onReceive(Context context, Intent ıntent) {


        Log.e("runn1","alarm creater çalıştı");

        preferences= PreferenceManager.getDefaultSharedPreferences(context);    //pref i çektik
        String program=preferences.getString("program",null);           //programı çektik
        JSONArray jsonArray;
        int pendingID=preferences.getInt("pendingID",0);//pending id ve artis miktarini cektik
        int artisMiktari=preferences.getInt("artisMiktari",0);
        int pId=pendingID;

        Log.e("pending id","pid1:"+pendingID+" artisMik:"+artisMiktari);
        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        // KAYITLI ALARM VARSA SİLİYORUZ  . . . . . .. .  .. . . .. .
        for(int i=0 ; i<artisMiktari ;i++,pendingID--){
            Log.e("for1","for1 e girdi");

            boolean alarmUp = (PendingIntent.getBroadcast(context, pendingID,
                    new Intent(context,AlarmReceiver.class),
                    PendingIntent.FLAG_NO_CREATE) != null);

            if(alarmUp){
                Log.e("alarmup","alarmup a girdi");

                Intent myIntent = new Intent(context,
                        AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context, pendingID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.cancel(pendingIntent);
                Log.e("runn2","alarmcreater da alarm silindi");
            }
        }

        Log.e( "pid","pendingId"+String.valueOf(pendingID));

        try {   //Ders programı çekiliyor json objelerden tarih ve saat bilgileri alınıp alarm manager kuruluyor

            Log.e("try","try'a girdi");

            jsonArray=new JSONArray(program);
            artisMiktari=0;

            for(int i=0 ; i<jsonArray.length() ; i++){

                Log.e("for2","for2 ye girdi"+"   jsonarraylength"+jsonArray.length());
                JSONObject object=jsonArray.getJSONObject(i);
                String saat=(String)object.get("saat");



                String tarih=(String)object.get("tarih");
                String date=tarih+"/"+saat;
                Calendar calendar= Calendar.getInstance();
                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy/HH:mm",new Locale("tr"));
                Date date1=sdf.parse(date);
               // date1.setTime(date1.getTime()-5*1000*60); // 5 dakika önce haber vermesi için date'ten 5 dk eksiltiorz

                calendar.setTime(date1);
                if(Calendar.getInstance().before(calendar)){
                    //mevcut tarih kullanıcının girdiği tarihten önce ise şu işlemleri yap
                    Log.e("for2 if ","for2 ife girdi");
                    pId=pId+1;
                    artisMiktari=artisMiktari+1;
                    Intent intent = new Intent(context, AlarmReceiver.class);
                    intent.putExtra("konu",object.get("konu").toString());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, pId, intent, 0);
                    // AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                   // alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                    Log.e("runn3","alarmcreaterde manager set edildi ve before çalıştı");
                    Log.e("before içi","Calendar.time :"+calendar.getTime().toString()+" || pendingID: "+pId +"konu:"+object.get("konu").toString());


                }
            }
            Kaydet(pId,artisMiktari);

        }catch (Exception e){ e.printStackTrace();
            Log.e("run4","alarm creater catch'e girdi");
        }

        Log.e("ppp","pending id :"+pendingID+" artismik: "+artisMiktari);
    }

    private void Kaydet(int pendingID , int artisMiktari){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("pendingID",pendingID);
        editor.putInt("artisMiktari",artisMiktari);
        editor.apply();
        Log.e("kaydet","pending id:"+pendingID+"artis miktari :"+artisMiktari);
    }





}
