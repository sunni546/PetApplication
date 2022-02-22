package com.example.pet;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Chart_Action extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_action);

        //BarEntry : (x,y 쌍으로 Bar Chart에 표시될 데이터 저장)
        //BarDataSet : BarEntry를 바탕으로 실제 Bar DateSet 생성
        //BarData : BarChart에 보여질 데이터 구성

        Button cancel_btn = (Button) findViewById(R.id.button_back);
        cancel_btn.setOnClickListener(view -> {
            finish();
        });

        //수면시간 차드
        BarChart barChart = (BarChart) findViewById(R.id.sleep_chart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(2f, 3));
        entries.add(new BarEntry(18f, 4));
        entries.add(new BarEntry(9f, 5));
        entries.add(new BarEntry(16f, 6));
        entries.add(new BarEntry(5f, 7));
        entries.add(new BarEntry(3f, 8));
        entries.add(new BarEntry(7f, 10));
        entries.add(new BarEntry(9f, 11));
        BarDataSet dataSet = new BarDataSet(entries, "# of Calls");
        BarData data = new BarData(dataSet);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS); //
        barChart.setData(data);


        BarChart barChart2= (BarChart) findViewById(R.id.eat_chart);
        ArrayList<BarEntry> entries2 = new ArrayList<>();
        entries2.add(new BarEntry(4f, 0));
        entries2.add(new BarEntry(8f, 1));
        entries2.add(new BarEntry(6f, 2));
        entries2.add(new BarEntry(2f, 3));
        entries2.add(new BarEntry(18f, 4));
        entries2.add(new BarEntry(9f, 5));
        entries2.add(new BarEntry(16f, 6));
        entries2.add(new BarEntry(5f, 7));
        entries2.add(new BarEntry(3f, 8));
        entries2.add(new BarEntry(7f, 10));
        entries2.add(new BarEntry(9f, 11));
        BarDataSet dataSet2 = new BarDataSet(entries2, "# of Calls");
        BarData data2 = new BarData(dataSet2);
        dataSet2.setColors(ColorTemplate.COLORFUL_COLORS); //
        barChart2.setData(data2);


        BarChart barChart3= (BarChart) findViewById(R.id.act_chart);
        ArrayList<BarEntry> entries3 = new ArrayList<>();
        entries3.add(new BarEntry(4f, 0));
        entries3.add(new BarEntry(8f, 1));
        entries3.add(new BarEntry(6f, 2));
        entries3.add(new BarEntry(2f, 3));
        entries3.add(new BarEntry(18f, 4));
        entries3.add(new BarEntry(9f, 5));
        entries3.add(new BarEntry(16f, 6));
        entries3.add(new BarEntry(5f, 7));
        entries3.add(new BarEntry(3f, 8));
        entries3.add(new BarEntry(7f, 10));
        entries3.add(new BarEntry(9f, 11));
        BarDataSet dataSet3 = new BarDataSet(entries3, "# of Calls");
        BarData data3 = new BarData(dataSet3);
        dataSet3.setColors(ColorTemplate.COLORFUL_COLORS); //
        barChart3.setData(data3);
    }
}
