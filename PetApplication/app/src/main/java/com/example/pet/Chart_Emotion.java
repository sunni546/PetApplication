package com.example.pet;


import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Chart_Emotion extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_emotion);

        //취소 버튼
        Button cancel_btn = (Button) findViewById(R.id.button_b);
        cancel_btn.setOnClickListener(view -> {
            finish();
        });


        LineChart lineChart = (LineChart) findViewById(R.id.happy_chart);
        LineChart lineChart2 = (LineChart) findViewById(R.id.comfort_chart);
        LineChart lineChart3 = (LineChart) findViewById(R.id.anxiety_chart);
        LineChart lineChart4 = (LineChart) findViewById(R.id.angry_chart);
        LineChart lineChart5 = (LineChart) findViewById(R.id.fear_chart);
        LineChart lineChart6 = (LineChart) findViewById(R.id.agg_chart);
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(2f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(9f, 5));
        entries.add(new Entry(16f, 6));
        entries.add(new Entry(5f, 7));
        entries.add(new Entry(3f, 8));
        entries.add(new Entry(7f, 10));
        entries.add(new Entry(9f, 11));

        LineDataSet dataSet = new LineDataSet(entries, "# of Calls");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        labels.add("July");
        labels.add("August");
        labels.add("September");
        labels.add("October");
        labels.add("November");
        labels.add("December");

        LineData data = new LineData(dataSet);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS); //


        lineChart.setData(data);
        lineChart.animateY(5000);

        lineChart2.setData(data);
        lineChart2.animateY(5000);

        lineChart3.setData(data);
        lineChart3.animateY(5000);

        lineChart4.setData(data);
        lineChart4.animateY(5000);

        lineChart5.setData(data);
        lineChart5.animateY(5000);

        lineChart6.setData(data);
        lineChart6.animateY(5000);

    }
}
