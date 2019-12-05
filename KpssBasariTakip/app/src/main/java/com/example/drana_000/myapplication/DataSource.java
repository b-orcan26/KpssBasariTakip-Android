package com.example.drana_000.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class DataSource {
    private SQLiteDatabase db;
    private  DbGateway dbGateway;

    public DataSource(Context context){
        dbGateway=new DbGateway(context);
    }

    public void openDatabase(){
            db=dbGateway.getWritableDatabase();
    }

    public void closeDatabase(){
        dbGateway.close();
    }

    public void addSoru(int dersid,int dogruS,int yanlisS,int bosS){

        String arguman=Integer.toString(dersid);
        String[] stunlar = {"dogruSayisi","yanlisSayisi","bosSayisi"};
        Cursor cursor = db.query("Derslers", stunlar,"dersID=?",
                new String[]{arguman},null,null,null);


        int dogruSayisi=0,yanlisSayisi=0,bosSayisi=0;
        while(cursor.moveToNext()){
            dogruSayisi=dogruSayisi+cursor.getInt(0);
            yanlisSayisi=yanlisSayisi+cursor.getInt(1);
            bosSayisi=bosSayisi+cursor.getInt(2);
        }
        cursor.close();
        dogruSayisi=dogruSayisi+dogruS;
        yanlisSayisi=yanlisSayisi+yanlisS;
        bosSayisi=bosSayisi+bosS;

        ContentValues cv=new ContentValues();
        cv.put("dogruSayisi",dogruSayisi);
        cv.put("yanlisSayisi",yanlisSayisi);
        cv.put("bosSayisi",bosSayisi);

        String where="dersid="+dersid;
        db.update("Derslers",cv,where,null);
        db.close();

    }

    public List<Dersler> selectSoru(){
        Cursor cursor=db.rawQuery("SELECT* FROM Derslers",null);
        List<Dersler> derslerList=new ArrayList<>();

        while(cursor.moveToNext()){
            Dersler ders=new Dersler();
            ders.setDersId(cursor.getInt(0));
            ders.setDersAd(cursor.getString(1));
            ders.setDogruSayisi(cursor.getInt(2));
            ders.setYanlisSayisi(cursor.getInt(3));
            ders.setBosSayisi(cursor.getInt(4));
            int sorusayisi=ders.getBosSayisi()+ders.getDogruSayisi()+ders.getYanlisSayisi();
            ders.setSoruSayisi(sorusayisi);
            derslerList.add(ders);
        }
        cursor.close();
        return derslerList;
    }

}
