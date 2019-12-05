package com.example.drana_000.myapplication;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class AnalizFragment extends Fragment {

    private View rootView;

    private DataSource dataSource;
    private List<Dersler> derslerList;
    private ProgressBar progressBar,progressBar2,progressBar3,progressBar4,progressBar5,progressBar6;
    private TextView tv5,tv6,tv7,tv8,tv9,tv10,tv11,tv12,tv13,tv14,tv15,tv16;
    private BarChart barChart;
    private float barWidth,barSpace,groupSpace;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_analiz, container, false);
        init();
        TextView[] dersİsimleriTvDizisi={tv5,tv7,tv9,tv11,tv13,tv15};
        TextView[] yuzdeTvDizisi={tv6,tv8,tv10,tv12,tv14,tv16};
        ProgressBar[] progressBarDizisi={progressBar,progressBar2,progressBar3,progressBar4,progressBar5,progressBar6};

        veritabaniAc();
        derslerList=dersleriVeritabanindanCek();
        int groupCount=derslerList.size();
        ArrayList xValues=dersleriListeyeAta(derslerList);
        ArrayList yValues1=dogrulariListeyeAta(derslerList);
        ArrayList yValues2=yanlislariListeyeAta(derslerList);
        ArrayList yValues3=boslariListeyeAta(derslerList);

        BarDataSet set1, set2 ,set3;
        set1 = verileriBarlaraAta(yValues1,"Doğru Sayısı");
        set1.setValueTextSize(5f);
        set1.setColor(Color.parseColor("#8bc34a"));
        set2 = verileriBarlaraAta(yValues2,"Yanlış Sayısı");
        set2.setValueTextSize(5f);
        set2.setColor(Color.parseColor("#cf4343"));
        set3 = verileriBarlaraAta(yValues3,"Boş Sayısı");
        set3.setValueTextSize(5f);
        set3.setColor(Color.parseColor("#c4c2ad"));

        BarData data=new BarData(set1,set2,set3);
        data.setValueFormatter(new LargeValueFormatter());
        barChart.setData(data);
        barChart.getBarData().setBarWidth(barWidth);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        barChart.groupBars(0, groupSpace, barSpace);
        barChart.getData().setHighlightEnabled(false);
        barChart.invalidate();

        //LEGEND ÖZELLİKLERİ

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setYOffset(20f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        //X-axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(6);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));

        //Y-axis
        barChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

        ////BURADAYIZZZZ

        dersleriTvlereAta(dersİsimleriTvDizisi,derslerList);
        for(int i=0 ; i<derslerList.size() ; i++){
            int dogru=derslerList.get(i).getDogruSayisi();
            Log.e    ("for",String.valueOf(dogru));
            int yanlis=derslerList.get(i).getYanlisSayisi();
            Log.e("for",String.valueOf(yanlis));
            int bos=derslerList.get(i).getBosSayisi();
            Log.e("for",String.valueOf(bos));
            double[] dizi = yuzdeHesapla(dogru,yanlis,bos);
            progressBarCizdir(dizi,progressBarDizisi[i]);
            yuzdeleriTvlereAta(yuzdeTvDizisi[i],dizi);
        }
        return rootView;




    }



    private BarDataSet verileriBarlaraAta(ArrayList list, String legend){

        return new BarDataSet(list,legend);
    }

    private ArrayList boslariListeyeAta(List<Dersler> liste){
        ArrayList yValues3=new ArrayList();

        for(int i=1 ; i<liste.size()+1 ; i++){
            yValues3.add(new BarEntry(i,liste.get(i-1).getBosSayisi()));
        }
        return yValues3;
    }

    private ArrayList yanlislariListeyeAta(List<Dersler> liste){
        ArrayList yValues2=new ArrayList();

        for(int i=1 ; i<liste.size()+1 ; i++){
            yValues2.add(new BarEntry(i,liste.get(i-1).getYanlisSayisi()));
        }
        return yValues2;
    }

    private ArrayList dogrulariListeyeAta(List<Dersler> liste){
        ArrayList yValues1=new ArrayList();

        for(int i=1 ; i<liste.size()+1 ; i++){
            yValues1.add(new BarEntry(i,liste.get(i-1).getDogruSayisi()));
        }
        return yValues1;
    }

    private ArrayList dersleriListeyeAta(List<Dersler> liste){
        ArrayList values=new ArrayList();

        for(int i=0 ; i <derslerList.size() ; i++){
            String dersAd=liste.get(i).getDersAd();
            values.add(dersAd.substring(0,3));
        }
        return values;
    }

    private void init(){

        dataSource=new DataSource(getActivity());
        progressBar=(ProgressBar)rootView.findViewById(R.id.progressBar);    progressBar.setMax(100);
        progressBar2=(ProgressBar)rootView.findViewById(R.id.progressBar2);  progressBar2.setMax(100);
        progressBar3=(ProgressBar)rootView.findViewById(R.id.progressBar3);  progressBar3.setMax(100);
        progressBar4=(ProgressBar)rootView.findViewById(R.id.progressBar4);  progressBar4.setMax(100);
        progressBar5=(ProgressBar)rootView.findViewById(R.id.progressBar5);  progressBar5.setMax(100);
        progressBar6=(ProgressBar)rootView.findViewById(R.id.progressBar6);  progressBar6.setMax(100);

        tv5=(TextView)rootView.findViewById(R.id.textView5);
        tv6=(TextView)rootView.findViewById(R.id.textView6);
        tv7=(TextView)rootView.findViewById(R.id.textView7);
        tv8=(TextView)rootView.findViewById(R.id.textView8);
        tv9=(TextView)rootView.findViewById(R.id.textView9);
        tv10=(TextView)rootView.findViewById(R.id.textView10);
        tv11=(TextView)rootView.findViewById(R.id.textView11);
        tv12=(TextView)rootView.findViewById(R.id.textView12);
        tv13=(TextView)rootView.findViewById(R.id.textView13);
        tv14=(TextView)rootView.findViewById(R.id.textView14);
        tv15=(TextView)rootView.findViewById(R.id.textView15);
        tv16=(TextView)rootView.findViewById(R.id.textView16);

        barWidth=0.2f;
        barSpace=0f;
        groupSpace=0.4f;



        barChart=(BarChart)rootView.findViewById(R.id.BarChart);

        barChart.setDescription(null);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);


    }

    private void dersleriTvlereAta(TextView[] tvDizi,List<Dersler> derslers){
        for(int i=0 ; i<tvDizi.length ; i++){

            Log.e("a",String.valueOf(tvDizi[i].getText()));
        }
    }

    private double[] yuzdeHesapla(int dogru,int yanlis,int bos){
        double toplam=dogru+yanlis+bos;
        if(toplam>=100){
            double katsayi=toplam/100;
            Log.e("katsayi",String.valueOf(katsayi));
            double dogruYuzde=dogru/katsayi;
            Log.e("yuzdeHesaplaDogru",String.valueOf(dogruYuzde));
            double yanlisYuzde=yanlis/katsayi;
            Log.e("yuzdeHesaplaYanlis",String.valueOf(yanlisYuzde));
            double bosYuzde=bos/katsayi;

            return new double[]{dogruYuzde,yanlisYuzde,bosYuzde};
        }
        else if(toplam==0){
            double dogruYuzde=0;
            double yanlisYuzde=0;
            double bosYuzde=0;
            return new double[]{dogruYuzde,yanlisYuzde,bosYuzde};
        }
        else{
            double katsayi=100/toplam;
            double dogruYuzde=dogru*katsayi;
            double yanlisYuzde=yanlis*katsayi;
            double bosYuzde=bos*katsayi;
            return new double[]{dogruYuzde,yanlisYuzde,bosYuzde};
        }

    }

    private void progressBarCizdir(double[] dizi,ProgressBar progressBar){
        int dogruYuzde=(int)Math.round(dizi[0]);
        int yanlisYuzde=(int)Math.round(dizi[1]);

        yanlisYuzde=yanlisYuzde+dogruYuzde;

        ObjectAnimator animator=ObjectAnimator.ofInt(progressBar,"Progress",dogruYuzde);
        animator.setDuration(1000);
        animator.start();

        animator=ObjectAnimator.ofInt(progressBar,"secondaryProgress",yanlisYuzde);
        animator.setDuration(1000);
        animator.start();
    }

    private void yuzdeleriTvlereAta(TextView tv,double[] dizi){
        //DecimalFormat df=new DecimalFormat("#+.#");
        // String dogru=df.format(dizi[0]);

        NumberFormat nf=NumberFormat.getInstance();
        nf.setMaximumFractionDigits(1);
        nf.setMaximumFractionDigits(0);
        String dogru=nf.format(dizi[0]);
        String yanlis=nf.format(dizi[1]);
        String bos=nf.format(dizi[2]);


        /*
        double d0=Double.valueOf(dogru);

        String yanlis=df.format(dizi[1]);
        double d1=Double.valueOf(yanlis);

        String bos=df.format(dizi[2]);
        double d2=Double.valueOf(bos);*/

        tv.setText("Doğru %"+dogru+" ,"+" Yanlış %"+yanlis+" ,"+" Boş %"+bos);
    }

    private void veritabaniAc(){
        dataSource.openDatabase();
    }

    private List<Dersler> dersleriVeritabanindanCek(){
        return dataSource.selectSoru();
    }

}


