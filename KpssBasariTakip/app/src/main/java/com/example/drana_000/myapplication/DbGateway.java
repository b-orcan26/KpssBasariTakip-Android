package com.example.drana_000.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DbGateway extends SQLiteOpenHelper {


    public DbGateway(Context context) {
        super(context, "Dersler", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final int İLK_DEGER=0;
        sqLiteDatabase.execSQL("create Table Derslers(dersID INTEGER PRIMARY KEY,dersAD TEXT," +
                "dogruSayisi INTEGER,yanlisSayisi INTEGER,bosSayisi INTEGER);");

        ekle(1,"Matematik",İLK_DEGER,İLK_DEGER,İLK_DEGER,sqLiteDatabase);
        ekle(2,"Türkçe",İLK_DEGER,İLK_DEGER,İLK_DEGER,sqLiteDatabase);
        ekle(3,"Kimya",İLK_DEGER,İLK_DEGER,İLK_DEGER,sqLiteDatabase);
        ekle(4,"Biyoloji",İLK_DEGER,İLK_DEGER,İLK_DEGER,sqLiteDatabase);
        ekle(5,"Fizik",İLK_DEGER,İLK_DEGER,İLK_DEGER,sqLiteDatabase);
        ekle(6,"Geometri",İLK_DEGER,İLK_DEGER,İLK_DEGER,sqLiteDatabase);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //not upgrade
    }

    private void ekle(int dersid,String dersad,int dogruS,int yanlisS,int bosS,SQLiteDatabase db){

        ContentValues cv=new ContentValues();
        cv.put("dersID",dersid);
        cv.put("dersAD",dersad);
        cv.put("dogruSayisi",dogruS);
        cv.put("yanlisSayisi",yanlisS);
        cv.put("bosSayisi",bosS);
        long r=db.insert("Derslers",null,cv);
        if(r>-1){
            Log.e("basarili","ilk ekleme basarili");
        }
        else
            Log.e("tag","ilk ekleme basarisiz");
    }
}
