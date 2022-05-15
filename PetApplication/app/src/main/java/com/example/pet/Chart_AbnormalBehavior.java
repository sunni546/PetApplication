package com.example.pet;

import static com.example.pet.Info.format_yyMMdd_HH;
import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Chart_AbnormalBehavior extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String format_yyMMdd = "yyMMdd";
    String n;
    Map<String, Object> Abnormal2 = new HashMap<>();
    String ACount_st2;
    String Time2;
    public static Integer Total_a;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userUid;
    String petNameStr;
    TextView tv1HourAb;
    TextView tv1dDayAb;

    Date currentTime;
    SimpleDateFormat format;
    SimpleDateFormat format2;
    LineChart lineChart1HourAb;
    LineChart lineChart1DayAb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_abnormal_behavior);

        currentTime = Calendar.getInstance().getTime();
        format = new SimpleDateFormat(format_yyMMdd_HH, Locale.getDefault());
        format2 = new SimpleDateFormat(format_yyMMdd, Locale.getDefault());
        String currentTimeStr = format.format(currentTime);
        String currentTimeStr2 = format2.format(currentTime);

        // Home 화면에서 pet 이름 받아오기
        Intent intentHome = new Intent(this.getIntent());
        petNameStr = intentHome.getStringExtra("petName");
        Log.d("NAME", "Pet name : " + petNameStr);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userUid = user.getUid();
        Total_a = 0;

        // 뒤로가기 버튼
        ImageButton btnAbBack = findViewById(R.id.btn_ab_back);
        btnAbBack.setOnClickListener(view -> {
            // pet 정보 화면으로 넘어가기
            finish();
        });

        // 이상 행동 통계
        tv1HourAb = (TextView) findViewById(R.id.tv_1hour_ab);


        //이상행동 횟수 구하기
        DocumentReference docRef = db.collection("Users").document(userUid)
                .collection("Pets").document(petNameStr)
                .collection("AbnormalBehaviors").document(currentTimeStr);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if (document.exists()){
                    Abnormal2=document.getData();
                    ACount_st2 = String.valueOf(Abnormal2.get("count"));
                    Time2 = String.valueOf(Abnormal2.get("time"));
                    tv1HourAb.setText(ACount_st2);
                }
            }
        });

        // 그래프
        //------------------------------------------------------------------------------------------
        // 이상 행동 1시간 그래프
        lineChart1HourAb = findViewById(R.id.chart_1hour_ab);

        db.collection("Users").document(userUid)
                .collection("Pets").document(petNameStr)
                .collection("AbnormalBehaviors")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Entry> entries = new ArrayList<>();
                        for (int i = 0; i < 6; i++) { entries.add(new BarEntry(i, 0)); }

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String documentNameStr = document.getId();
                            if (documentNameStr.equals(subtract1Hour(5))) {
                                entries.set(0, new BarEntry(0, parseInt(String.valueOf(document.get("count")))));
                            } else if (documentNameStr.equals(subtract1Hour(4))) {
                                entries.set(1, new BarEntry(1, parseInt(String.valueOf(document.get("count")))));
                            } else if (documentNameStr.equals(subtract1Hour(3))) {
                                entries.set(2, new BarEntry(2, parseInt(String.valueOf(document.get("count")))));
                            } else if (documentNameStr.equals(subtract1Hour(2))) {
                                entries.set(3, new BarEntry(3, parseInt(String.valueOf(document.get("count")))));
                            } else if (documentNameStr.equals(subtract1Hour(1))) {
                                entries.set(4, new BarEntry(4, parseInt(String.valueOf(document.get("count")))));
                            } else if (documentNameStr.equals(currentTimeStr)) {
                                entries.set(5, new BarEntry(5, parseInt(String.valueOf(document.get("count")))));
                            }
                        }

                        // x축 라벨
                        ArrayList<String> labels = new ArrayList<>();
                        labels.add("5h");
                        labels.add("4h");
                        labels.add("3h");
                        labels.add("2h");
                        labels.add("1h");
                        labels.add("0");

                        LineDataSet dataset = new LineDataSet(entries, "시간별 이상행동 횟수");
                        dataset.setColors(Color.GREEN);

                        // 차트에 데이터 입력
                        LineData data = new LineData(dataset);
                        lineChart1HourAb.setData(data);

                        // x축 설정
                        XAxis xAxis = lineChart1HourAb.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setAxisMinimum(0);
                        xAxis.setLabelCount(10);

                        // y축 설정
                        // 왼쪽 y축 제거
                        YAxis yLAxis = lineChart1HourAb.getAxisLeft();
                        yLAxis.setDrawLabels(false);
                        yLAxis.setDrawAxisLine(false);
                        yLAxis.setDrawGridLines(false);

                        lineChart1HourAb.setDoubleTapToZoomEnabled(false);
                        lineChart1HourAb.setDrawGridBackground(false);

                        Description description = new Description();
                        description.setText("회");
                        lineChart1HourAb.setDescription(description);

                        lineChart1HourAb.animateY(1000); // 애니메이션 설정
                        lineChart1HourAb.invalidate();
                    } else {
                        Log.d("CHARTAB", "Error getting documents: ", task.getException());
                    }
                });

        // 이상 행동 1일 그래프
        lineChart1DayAb = (LineChart) findViewById(R.id.chart_1day_ab);

        db.collection("Users").document(userUid)
                .collection("Pets").document(petNameStr)
                .collection("AbnormalBehaviors")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Entry> entries = new ArrayList<>();

                        int y0 = 0; int y1 = 0; int y2 = 0; int y3 = 0; int y4 = 0; int y5 = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String documentNameStr = document.getId().substring(0, 6);
                            if (documentNameStr.equals(subtract1Day(5))) { y0 += parseInt(String.valueOf(document.get("count"))); }
                            else if (documentNameStr.equals(subtract1Day(4))) { y1 += parseInt(String.valueOf(document.get("count"))); }
                            else if (documentNameStr.equals(subtract1Day(3))) { y2 += parseInt(String.valueOf(document.get("count"))); }
                            else if (documentNameStr.equals(subtract1Day(2))) { y3 += parseInt(String.valueOf(document.get("count"))); }
                            else if (documentNameStr.equals(subtract1Day(1))) { y4 += parseInt(String.valueOf(document.get("count"))); }
                            else if (documentNameStr.equals(currentTimeStr2)) { y5 += parseInt(String.valueOf(document.get("count"))); }
                        }

                        tv1dDayAb = (TextView) findViewById(R.id.tv_1day_ab);
                        tv1dDayAb.setText(String.valueOf(y5));

                        entries.add(new Entry(0, y0));
                        entries.add(new Entry(1, y1));
                        entries.add(new Entry(2, y2));
                        entries.add(new Entry(3, y3));
                        entries.add(new Entry(4, y4));
                        entries.add(new Entry(5, y5));

                        Log.d("CHARTAB", String.valueOf(entries));

                        // x축 라벨
                        ArrayList<String> labels = new ArrayList<>();
                        labels.add("5d");
                        labels.add("4d");
                        labels.add("3d");
                        labels.add("2d");
                        labels.add("1d");
                        labels.add("0");

                        LineDataSet dataset = new LineDataSet(entries, "일별 이상행동 횟수");
                        dataset.setColors(Color.GREEN);

                        // 차트에 데이터 입력
                        LineData data = new LineData(dataset);
                        lineChart1DayAb.setData(data);

                        // x축 설정
                        XAxis xAxis = lineChart1DayAb.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setAxisMinimum(0);
                        xAxis.setLabelCount(10);

                        // y축 설정
                        // 왼쪽 y축 제거
                        YAxis yLAxis = lineChart1DayAb.getAxisLeft();
                        yLAxis.setDrawLabels(false);
                        yLAxis.setDrawAxisLine(false);
                        yLAxis.setDrawGridLines(false);

                        lineChart1DayAb.setDoubleTapToZoomEnabled(false);
                        lineChart1DayAb.setDrawGridBackground(false);

                        Description description = new Description();
                        description.setText("회");
                        lineChart1DayAb.setDescription(description);

                        lineChart1DayAb.animateY(1000); // 애니메이션 설정
                        lineChart1DayAb.invalidate();
                    } else {
                        Log.d("CHARTAB", "Error getting documents: ", task.getException());
                    }
                });
        //------------------------------------------------------------------------------------------
    }

    protected String subtract1Hour(int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        cal.add(Calendar.HOUR, -(i));
        return format.format(cal.getTime());
    }

    protected String subtract1Day(int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        cal.add(Calendar.DATE, -(i));
        return format2.format(cal.getTime());
    }
}