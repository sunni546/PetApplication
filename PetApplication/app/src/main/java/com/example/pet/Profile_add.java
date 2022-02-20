package com.example.pet;


import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

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

public class Profile_add extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_add);

        //취소 버튼
        Button cancel_btn = (Button) findViewById(R.id.cancel_btn_add);
        cancel_btn.setOnClickListener(view -> {
            finish();
        });

        // 저장 버튼
        Button save_btn = (Button) findViewById(R.id.save_btn_add);
        save_btn.setOnClickListener(view -> {
            finish();
        });

    }
}
