package com.example.drana_000.myapplication;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


public class DersProgramiFragment extends Fragment {

    private View rootView;

    private Button btn_Temizle;
    private Button btn_Kontrol;
    private TableLayout tblLayout;
    private SharedPreferences preferences;


    @Override
    public void onStart() {
        super.onStart();

        String str = preferences.getString("row1","");
        if(str.equals("")){
            //tablo yükleme
            Log.e("row1 kontrol","boş");
        }
        else {
            Log.e("row1 kontrol","boş değil");
            tabloYükle();
        }
        int ab=preferences.getInt("pendingID",0);
        if(ab==0){
            Log.e("main4Start","null");
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("pendingID",0);
            editor.putInt("artisMiktari",0);
            editor.apply();
        }else{ Log.e("main4Start","null deil"); }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_ders_programi, container, false);
        init();
        btn_Temizle_Click();
        btn_Kontrol_Click();
        return rootView;
    }

    private void init(){

        ImageButton btn_time1,btn_time2,btn_time3,btn_time4,btn_time5,btn_time6,btn_time7,
                btn_date1,btn_date2,btn_date3,btn_date4,btn_date5,btn_date6,btn_date7;


        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String[][] diziTabloKonum=new String[8][8];

        tblLayout=(TableLayout)rootView.findViewById(R.id.tblLayout);

        btn_time1=(ImageButton)rootView.findViewById(R.id.btn_time1);
        btn_time2=(ImageButton)rootView.findViewById(R.id.btn_time2);
        btn_time3=(ImageButton)rootView.findViewById(R.id.btn_time3);
        btn_time4=(ImageButton)rootView.findViewById(R.id.btn_time4);
        btn_time5=(ImageButton)rootView.findViewById(R.id.btn_time5);
        btn_time6=(ImageButton)rootView.findViewById(R.id.btn_time6);
        btn_time7=(ImageButton)rootView.findViewById(R.id.btn_time7);

        btn_time1.setOnClickListener(new DersProgramiFragment.timeClickHandler());
        btn_time2.setOnClickListener(new DersProgramiFragment.timeClickHandler());
        btn_time3.setOnClickListener(new DersProgramiFragment.timeClickHandler());
        btn_time4.setOnClickListener(new DersProgramiFragment.timeClickHandler());
        btn_time5.setOnClickListener(new DersProgramiFragment.timeClickHandler());
        btn_time6.setOnClickListener(new DersProgramiFragment.timeClickHandler());
        btn_time7.setOnClickListener(new DersProgramiFragment.timeClickHandler());

        btn_date1=(ImageButton)rootView.findViewById(R.id.btn_date1);
        btn_date2=(ImageButton)rootView.findViewById(R.id.btn_date2);
        btn_date3=(ImageButton)rootView.findViewById(R.id.btn_date3);
        btn_date4=(ImageButton)rootView.findViewById(R.id.btn_date4);
        btn_date5=(ImageButton)rootView.findViewById(R.id.btn_date5);
        btn_date6=(ImageButton)rootView.findViewById(R.id.btn_date6);
        btn_date7=(ImageButton)rootView.findViewById(R.id.btn_date7);

        btn_date1.setOnClickListener(new DersProgramiFragment.dateClickHandler());
        btn_date2.setOnClickListener(new DersProgramiFragment.dateClickHandler());
        btn_date3.setOnClickListener(new DersProgramiFragment.dateClickHandler());
        btn_date4.setOnClickListener(new DersProgramiFragment.dateClickHandler());
        btn_date5.setOnClickListener(new DersProgramiFragment.dateClickHandler());
        btn_date6.setOnClickListener(new DersProgramiFragment.dateClickHandler());
        btn_date7.setOnClickListener(new DersProgramiFragment.dateClickHandler());

        btn_Temizle=(Button)rootView.findViewById(R.id.buttonTemizle);
        btn_Kontrol=(Button)rootView.findViewById(R.id.buttonKontrol);
    }



    private void btn_Temizle_Click(){
        btn_Temizle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("program",null);
                editor.putString("row1",""); editor.putString("row5","");
                editor.putString("row2",""); editor.putString("row6","");
                editor.putString("row3",""); editor.putString("row7","");
                editor.putString("row4",""); editor.putString("row8","");
                editor.apply();

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(DersProgramiFragment.this).attach(DersProgramiFragment.this).commit();
            }
        });
    }

    private void btn_Kontrol_Click(){
        btn_Kontrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean b=tableControl();
                if (b) {
                    for(int i=2;i<tblLayout.getChildCount();i++){
                        TableRow row=(TableRow)tblLayout.getChildAt(i);
                        for(int j=2 ; j<row.getChildCount() ; j++){
                            EditText et=(EditText)row.getChildAt(j);
                            et.setClickable(false);
                            et.setFocusable(false);
                            et.setFocusableInTouchMode(false);
                        }
                    }
                    btn_Kontrol.setText(" Kaydet ");
                    btn_Kontrol.setOnClickListener(new DersProgramiFragment.saveClickHandler());
                }
                else{
                    Toast.makeText(rootView.getContext(),"Tarih veya Saat Seçmediniz",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class saveClickHandler implements  View.OnClickListener{
        @Override
        public void onClick(View view) {
            JSONArray jsonArray=tablodakiVerileriAl();
            String str=jsonArray.toString();
            tabloyuKaydet(str);
            tabloyuKaydetKonumlu();
            Toast.makeText(rootView.getContext(),"Ders Programı Kaydedildi",Toast.LENGTH_SHORT).show();
            //LocalBroadcastManager localBrManager=LocalBroadcastManager.getInstance(rootView.getContext());
            Intent intent = new Intent(rootView.getContext(), AlarmCreater.class);
            intent.putExtra("mesaj", "Ders programi degisti");
            getContext().sendBroadcast(intent);
            //getContext().sendBroadcast(intent);
            //burada bir değişiklik

        }
    }

    public class timeClickHandler implements View.OnClickListener{
        View view1;

        @Override
        public void onClick(View view) {
            final Calendar takvim = Calendar.getInstance();
            int saat = takvim.get(Calendar.HOUR_OF_DAY);
            int dakika = takvim.get(Calendar.MINUTE);
            view1=view;
            TimePickerDialog tpd = new TimePickerDialog(rootView.getContext(),
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            tvSaatDegistir(hourOfDay,minute,view1);
                        }
                    }, saat, dakika, true);
            tpd.setButton(TimePickerDialog.BUTTON_POSITIVE, "Seç", tpd);
            tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE, "İptal", tpd);
            tpd.show();
        }
    }

    public class dateClickHandler implements View.OnClickListener{
        View view1;

        @Override
        public void onClick(View view) {
            Calendar mcurrentTime = Calendar.getInstance();
            int year = mcurrentTime.get(Calendar.YEAR);//Güncel Yılı alıyoruz
            int month = mcurrentTime.get(Calendar.MONTH);//Güncel Ayı alıyoruz
            int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz
            view1=view;
            DatePickerDialog datePicker;//Datepicker objemiz
            datePicker = new DatePickerDialog(rootView.getContext(), new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    tvTarihDegistir( dayOfMonth,monthOfYear+1,year,view1);
                }
            },year,month,day);//başlarken set edilcek değerlerimizi atıyoruz
            datePicker.setTitle("Tarih Seçiniz");
            datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", datePicker);
            datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

            datePicker.show();
        }
    }

    public void tvTarihDegistir(int dayOfMonth,int monthOfYear,int year , View view){
        int position=0;

        TableRow row=(TableRow)view.getParent();
        for(int i=0 ; i<row.getChildCount() ; i++){
            if(row.getChildAt(i)==view){
                position=i;
            }
        }

        TableRow row1=(TableRow)tblLayout.getChildAt(1);
        TextView tv=(TextView)row1.getChildAt(position);
        tv.setText(dayOfMonth + "/" + monthOfYear+ "/"+year);

    }

    public void tvSaatDegistir(int hourOfDay , int minute ,View view){

        //POSİTİON I ALMIŞ OLDUK
        TableRow row=(TableRow)view.getParent();
        TextView tv=(TextView)row.getChildAt(1);
        tv.setText(hourOfDay + ":" + minute);
    }

    private boolean tableControl(){
        int hata=0;
        for(int i=2 ; i<tblLayout.getChildCount() ;i++){
            TableRow row=(TableRow)tblLayout.getChildAt(i);
            for(int j=2 ; j<row.getChildCount() ; j++){
                EditText et=(EditText)row.getChildAt(j);
                if(!et.getText().toString().trim().equals("")){
                    TextView tv=(TextView)row.getChildAt(1);
                    TableRow rowTemp=(TableRow)tblLayout.getChildAt(1);
                    TextView tvTemp=(TextView)rowTemp.getChildAt(j);    //
                    if(tv.getText().toString().equals(" Saat ")|| tvTemp.getText().toString().equals(" Tarih ")){
                        hata++;
                    }
                    else {
                        Log.e("tag","//"+tv.getText().toString()+"//"+tvTemp.getText().toString()+"//");
                    }
                }
            }
        }


        if(hata>0){
            return false;
        }
        else{ return true; }

//son
    }

    private JSONArray tablodakiVerileriAl(){
        JSONArray jsonArray=new JSONArray();

        for(int i=2 ; i<tblLayout.getChildCount() ;i++){
            TableRow row=(TableRow)tblLayout.getChildAt(i); //2.satırda edittextler başıyor o yuzden i 3 ten başladı
            for(int j=2 ; j<row.getChildCount() ; j++){     //2. sütunda edittextler başlıyor ondan j=2
                EditText et=(EditText)row.getChildAt(j);    // edittextleri çekiyoruz
                if(!et.getText().toString().trim().equals("")){     //etnin text inde bir karakter varsa
                    TextView tv=(TextView)row.getChildAt(1);  // aynı satırdaki tv yi aldık
                    TableRow rowTemp=(TableRow)tblLayout.getChildAt(1);    // aynı sütundaki tv yi aldık
                    TextView tvTemp=(TextView)rowTemp.getChildAt(j);    //
                    if(!tv.getText().toString().equals(" Saat ")|| !tvTemp.getText().toString().equals(" Tarih ")){
                        try {
                            JSONObject jsonObject=new JSONObject();
                            jsonObject.put("saat",tv.getText().toString());
                            jsonObject.put("tarih",tvTemp.getText().toString());
                            jsonObject.put("konu",et.getText().toString());
                            jsonArray.put(jsonObject);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }
            }
        }
        return jsonArray;

    }

    private void tabloyuKaydet(String str){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("program",str);
        editor.apply();
    }

    private void tabloYükle(){


        String row1 = preferences.getString("row1",null);

        Log.e("row1","row1 nulll");
        try {
            JSONArray jarr = new JSONArray(row1);
            TableRow rowsS = (TableRow) tblLayout.getChildAt(1);
            for (int i = 2; i < 9; i++) {
                TextView tv = (TextView) rowsS.getChildAt(i);
                tv.setText(jarr.getJSONObject(i - 2).get(String.valueOf(i - 2)).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String row2 = preferences.getString("row2", null);
        tabloyuDiz(row2, 2);
        String row3 = preferences.getString("row3", null);
        tabloyuDiz(row3, 3);
        String row4 = preferences.getString("row4", null);
        tabloyuDiz(row4, 4);
        String row5 = preferences.getString("row5", null);
        tabloyuDiz(row5, 5);
        String row6 = preferences.getString("row6", null);
        tabloyuDiz(row6, 6);
        String row7 = preferences.getString("row7", null);
        tabloyuDiz(row7, 7);
        String row8 = preferences.getString("row8", null);
        tabloyuDiz(row8, 8);

    }

    private void tabloyuDiz(String str,int index){
        try{
            JSONArray jar=new JSONArray(str);
            TableRow rows=(TableRow)tblLayout.getChildAt(index);
            TextView tv=(TextView)rows.getChildAt(1);
            tv.setText(jar.getJSONObject(0).get(String.valueOf(0)).toString());
            for(int i=2 ; i<9 ; i++){
                EditText et=(EditText)rows.getChildAt(i);
                et.setText(jar.getJSONObject(i-1).get(String.valueOf(i-1)).toString());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void tabloyuKaydetKonumlu(){

        JSONArray jarRow1=new JSONArray();
        TableRow row1=(TableRow)tblLayout.getChildAt(1);
        for(int i=2 ; i<row1.getChildCount() ; i++){
            JSONObject job=new JSONObject();
            TextView tv=(TextView)row1.getChildAt(i);
            try {
                job.put(String.valueOf(i-2),tv.getText().toString());
                jarRow1.put(job);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SharedPreferences.Editor editor=preferences.edit();

        String jrow1=jarRow1.toString();    editor.putString("row1",jrow1);
        String row2=degerAl(2);       editor.putString("row2",row2);
        String row3=degerAl(3);       editor.putString("row3",row3);
        String row4=degerAl(4);       editor.putString("row4",row4);
        String row5=degerAl(5);       editor.putString("row5",row5);
        String row6=degerAl(6);       editor.putString("row6",row6);
        String row7=degerAl(7);       editor.putString("row7",row7);
        String row8=degerAl(8);       editor.putString("row8",row8);

        editor.apply();
    }

    private String degerAl(int index){
        JSONArray jar=new JSONArray();
        TableRow row=(TableRow)tblLayout.getChildAt(index);
        TextView tv=(TextView)row.getChildAt(1);
        JSONObject job=new JSONObject();
        try {
            job.put("0",tv.getText().toString());
            jar.put(job);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        for(int i=2 ; i<row.getChildCount() ; i++){
            JSONObject jsonObject=new JSONObject();
            EditText et=(EditText)row.getChildAt(i);
            try{
                jsonObject.put(String.valueOf(i-1),et.getText().toString());
                jar.put(jsonObject);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
       return jar.toString();

    }





}
