package com.example.pet;

import android.os.Bundle;
import android.widget.ImageButton;
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

public class Chart_AbnormalBehavior extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_abnormal_behavior);

        // 뒤로가기 버튼
        ImageButton btnAbBack = findViewById(R.id.btn_ab_back);
        btnAbBack.setOnClickListener(view -> {
            // pet 정보 화면으로 넘어가기
            finish();
        });

        // 이상 행동 통계
        TextView tv1HourAb = (TextView) findViewById(R.id.tv_1hour_ab);
        TextView tv1dDayAb = (TextView) findViewById(R.id.tv_1day_ab);

        // TODO
        String hourAbStr = "2";
        String dayAbStr = "3";

        tv1HourAb.setText(hourAbStr);
        tv1dDayAb.setText(dayAbStr);

        // 이상 행동 1시간 그래프
        LineChart lineChart1HourAb = (LineChart) findViewById(R.id.chart_1hour_ab);

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
        lineChart1HourAb.animateY(1500);
        lineChart1HourAb.setData(data);

        // 이상 행동 1일 그래프
        LineChart lineChart1DayAb = (LineChart) findViewById(R.id.chart_1day_ab);

        lineChart1DayAb.animateY(1500);
        lineChart1DayAb.setData(data);
    }
}