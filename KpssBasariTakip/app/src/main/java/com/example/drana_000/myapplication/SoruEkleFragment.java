package com.example.drana_000.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class SoruEkleFragment extends Fragment {

    private View rootView;
    private RadioGroup radioGroup;
    private RadioButton btn_Mat, btn_Bio, btn_Fiz , btn_Kim,btn_Geo,btn_Tur;
    private Button btn_Kaydet;
    private DataSource dataSource;
    private EditText et_DogruSayisi,et_YanlisSayisi,et_BosSayisi;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.layout_soru_ekle,container,false);
        init();
        btn_Kaydet_Click();
        return rootView;
    }

    private void init(){
        radioGroup=(RadioGroup)rootView.findViewById(R.id.radioGroup);
        btn_Mat=(RadioButton)rootView.findViewById(R.id.btn_MAT);
        btn_Bio=(RadioButton)rootView.findViewById(R.id.btn_BİO);
        btn_Fiz=(RadioButton)rootView.findViewById(R.id.btn_FİZ);
        btn_Kim=(RadioButton)rootView.findViewById(R.id.btn_KİM);
        btn_Geo=(RadioButton)rootView.findViewById(R.id.btn_GEO);
        btn_Tur=(RadioButton)rootView.findViewById(R.id.btn_TUR);
        btn_Kaydet=(Button)rootView.findViewById(R.id.button_Kaydet);
        dataSource=new DataSource(getContext());
        et_DogruSayisi=(EditText)rootView.findViewById(R.id.editText);
        et_YanlisSayisi=(EditText)rootView.findViewById(R.id.editText2);
        et_BosSayisi=(EditText)rootView.findViewById(R.id.EditText6);
    }

    private void btn_Kaydet_Click(){
        btn_Kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean dersSecim=radioGroupKontrol();
                if (!dersSecim)
                {
                    Toast.makeText(getContext(),"Bir ders seçiniz",Toast.LENGTH_SHORT).show();
                }
                else{
                    dataSource.openDatabase();
                    int dogruSayisi=getDogruSayisi();
                    int bosSayisi=getBosSayisi();
                    int yanlisSayisi=getYanlisSayisi();
                    if(radioGroup.getCheckedRadioButtonId()==btn_Mat.getId()){
                        dataSource.addSoru(1,dogruSayisi,yanlisSayisi,bosSayisi);
                        Toast.makeText(rootView.getContext(),"Cevaplarınız kaydedildi",Toast.LENGTH_SHORT).show();
                        etTemizle();
                    }
                    else if(radioGroup.getCheckedRadioButtonId()==btn_Bio.getId()){
                        dataSource.addSoru(4,dogruSayisi,yanlisSayisi,bosSayisi);
                        Toast.makeText(rootView.getContext(),"Cevaplarınız kaydedildi",Toast.LENGTH_SHORT).show();
                        etTemizle();
                    }
                    else if(radioGroup.getCheckedRadioButtonId()==btn_Fiz.getId()){
                        dataSource.addSoru(5,dogruSayisi,yanlisSayisi,bosSayisi);
                        Toast.makeText(rootView.getContext(),"Cevaplarınız kaydedildi",Toast.LENGTH_SHORT).show();
                        etTemizle();
                    }
                    else if(radioGroup.getCheckedRadioButtonId()==btn_Kim.getId()){
                        dataSource.addSoru(3,dogruSayisi,yanlisSayisi,bosSayisi);
                        Toast.makeText(rootView.getContext(),"Cevaplarınız kaydedildi",Toast.LENGTH_SHORT).show();
                        etTemizle();
                    }
                    else if(radioGroup.getCheckedRadioButtonId()==btn_Geo.getId()){
                        dataSource.addSoru(6,dogruSayisi,yanlisSayisi,bosSayisi);
                        Toast.makeText(rootView.getContext(),"Cevaplarınız kaydedildi",Toast.LENGTH_SHORT).show();
                        etTemizle();
                    }
                    else if(radioGroup.getCheckedRadioButtonId()==btn_Tur.getId()){
                        dataSource.addSoru(2,dogruSayisi,yanlisSayisi,bosSayisi);
                        Toast.makeText(rootView.getContext(),"Cevaplarınız kaydedildi",Toast.LENGTH_SHORT).show();
                        etTemizle();
                    }
                }
                dataSource.closeDatabase();
            }
        });
    }

    private boolean radioGroupKontrol(){
        if(radioGroup.getCheckedRadioButtonId()==-1){
            return false;
        }
        else{
            return true;
        }
    }

    private int getBosSayisi(){
        int bosSayisi=0;
        if(!et_BosSayisi.getText().toString().equals("")){
            bosSayisi = Integer.parseInt( et_BosSayisi.getText().toString() );
        }
        return bosSayisi;
    }

    private int getYanlisSayisi(){
        int yanlisSayisi=0;
        if(!et_YanlisSayisi.getText().toString().equals("")){
            yanlisSayisi = Integer.parseInt( et_YanlisSayisi.getText().toString() );
        }
        return yanlisSayisi;
    }

    private int getDogruSayisi(){
        int dogruSayisi=0;
        if(!et_DogruSayisi.getText().toString().equals("")){
            dogruSayisi = Integer.parseInt( et_DogruSayisi.getText().toString() );
        }
        return dogruSayisi;

    }

    private void etTemizle(){
        et_BosSayisi.getText().clear();
        et_YanlisSayisi.getText().clear();
        et_DogruSayisi.getText().clear();
    }





}
