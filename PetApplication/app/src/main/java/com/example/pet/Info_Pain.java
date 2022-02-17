package com.example.pet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Info_Pain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_pain);

        // 통증 통계
        TextView tv1HourPain = (TextView) findViewById(R.id.tv_1hour_pain);
        TextView tv1dDayPain = (TextView) findViewById(R.id.tv_1day_pain);

        // TODO
        String hourPainStr = "2";
        String dayPainStr = "3";

        tv1HourPain.setText(hourPainStr);
        tv1dDayPain.setText(dayPainStr);

        // 통증 1시간 그래프
        LineChart lc1HourPain = (LineChart) findViewById(R.id.chart_1hour_pain);

        // TODO
        ArrayList<Entry> entries = new ArrayList<>();
        // 그래프에 넣을 점 설정
        entries.add(new BarEntry(0, 1));
        entries.add(new BarEntry(1, 3));
        entries.add(new BarEntry(2, 2));
        entries.add(new BarEntry(3, 1));
        entries.add(new BarEntry(4, 4));
        entries.add(new BarEntry(5, 8));

        // 세로축 설정
        LineDataSet dataset = new LineDataSet(entries, "횟수");
        dataset.setAxisDependency(YAxis.AxisDependency.RIGHT);

        // 차트에 데이터 입력
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        LineData data = new LineData(dataset);

        // 애니메이션 설정
        // 설정시 그래프가 한번에 나타나는 것이 아니라 창 이동시 그래프의 선이 제자리 찾아가면서 나타남
        lc1HourPain.animateY(1500);
        lc1HourPain.setData(data);

        // 통증 1일 그래프
        LineChart lc1DayPain = (LineChart) findViewById(R.id.chart_1day_pain);

        lc1DayPain.animateY(1500);
        lc1DayPain.setData(data);
    }
}
