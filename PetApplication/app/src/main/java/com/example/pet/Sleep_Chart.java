package com.example.pet;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Sleep_Chart extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep);

        //BarEntry : (x,y 쌍으로 Bar Chart에 표시될 데이터 저장)
        //BarDataSet : BarEntry를 바탕으로 실제 Bar DateSet 생성
        //BarData : BarChart에 보여질 데이터 구성


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

    }
}
