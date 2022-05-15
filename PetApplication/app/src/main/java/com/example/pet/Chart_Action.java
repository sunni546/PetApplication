package com.example.pet;

import static android.content.ContentValues.TAG;

import static com.example.pet.Info.format_yyMMdd_HH;
import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
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

public class Chart_Action extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int count;
    String stringTime;

    String rest;
    int rest_t;

    String act;
    int act_t;

    TextView R_m;
    TextView A_m;

    Map<String, Object> Action = new HashMap<>();

    Date currentTime;
    SimpleDateFormat format;
    BarChart barChartRest;
    BarChart barChartAct;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_action);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userUid = user.getUid();
        Intent intentInfo = new Intent(this.getIntent());
        String petNameStr = intentInfo.getStringExtra("petName");

        // 뒤로가기 버튼
        ImageButton btnActionBack = (ImageButton) findViewById(R.id.btn_action_back);
        btnActionBack.setOnClickListener(view -> finish());

        currentTime = Calendar.getInstance().getTime();
        format = new SimpleDateFormat(format_yyMMdd_HH, Locale.getDefault());
        String currentTimeStr = format.format(currentTime);

        // 그래프
        //------------------------------------------------------------------------------------------
        barChartRest = findViewById(R.id.rest_chart);
        barChartAct = findViewById(R.id.act_chart);

        // 시간별 그래프 그리기
        db.collection("Users").document(userUid)
                .collection("Pets").document(petNameStr)
                .collection("Actions")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int Total_R=0;
                        int Total_A=0;
                        // x축 라벨
                        ArrayList<String> labels = new ArrayList<>();
                        labels.add("5h");
                        labels.add("4h");
                        labels.add("3h");
                        labels.add("2h");
                        labels.add("1h");
                        labels.add("0");

                        // 그래프에 값 추가
                        ArrayList<BarEntry> entriesRest = new ArrayList<>();
                        ArrayList<BarEntry> entriesAct = new ArrayList<>();

                        for (int i = 0; i < 6; i++) {
                            entriesRest.add(new BarEntry(i, 0));
                            entriesAct.add(new BarEntry(i, 0));
                        }

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String documentNameStr = document.getId();

                            if (documentNameStr.equals(subtract1Hour(5))) {
                                entriesRest.set(0, new BarEntry(0, secondToMinute(String.valueOf(document.get("휴식 시간")))));
                                entriesAct.set(0, new BarEntry(0, secondToMinute(String.valueOf(document.get("운동 시간")))));
                                Total_R=Total_R+ secondToMinute(String.valueOf(document.get("휴식 시간")));
                                Total_A=Total_A+secondToMinute(String.valueOf(document.get("운동 시간")));
                            } else if (documentNameStr.equals(subtract1Hour(4))) {
                                entriesRest.set(1, new BarEntry(1, secondToMinute(String.valueOf(document.get("휴식 시간")))));
                                entriesAct.set(1, new BarEntry(1, secondToMinute(String.valueOf(document.get("운동 시간")))));
                                Total_R=Total_R+ secondToMinute(String.valueOf(document.get("휴식 시간")));
                                Total_A=Total_A+secondToMinute(String.valueOf(document.get("운동 시간")));
                            } else if (documentNameStr.equals(subtract1Hour(3))) {
                                entriesRest.set(2, new BarEntry(2, secondToMinute(String.valueOf(document.get("휴식 시간")))));
                                entriesAct.set(2, new BarEntry(2, secondToMinute(String.valueOf(document.get("운동 시간")))));
                                Total_R=Total_R+ secondToMinute(String.valueOf(document.get("휴식 시간")));
                                Total_A=Total_A+secondToMinute(String.valueOf(document.get("운동 시간")));
                            } else if (documentNameStr.equals(subtract1Hour(2))) {
                                entriesRest.set(3, new BarEntry(3, secondToMinute(String.valueOf(document.get("휴식 시간")))));
                                entriesAct.set(3, new BarEntry(3, secondToMinute(String.valueOf(document.get("운동 시간")))));
                                Total_R=Total_R+ secondToMinute(String.valueOf(document.get("휴식 시간")));
                                Total_A=Total_A+secondToMinute(String.valueOf(document.get("운동 시간")));
                            } else if (documentNameStr.equals(subtract1Hour(1))) {
                                entriesRest.set(4, new BarEntry(4, secondToMinute(String.valueOf(document.get("휴식 시간")))));
                                entriesAct.set(4, new BarEntry(4, secondToMinute(String.valueOf(document.get("운동 시간")))));
                                Total_R=Total_R+ secondToMinute(String.valueOf(document.get("휴식 시간")));
                                Total_A=Total_A+secondToMinute(String.valueOf(document.get("운동 시간")));
                            } else if (documentNameStr.equals(currentTimeStr)) {
                                entriesRest.set(5, new BarEntry(5, secondToMinute(String.valueOf(document.get("휴식 시간")))));
                                entriesAct.set(5, new BarEntry(5, secondToMinute(String.valueOf(document.get("운동 시간")))));
                                Total_R=Total_R+ secondToMinute(String.valueOf(document.get("휴식 시간")));
                                Total_A=Total_A+secondToMinute(String.valueOf(document.get("운동 시간")));
                            }
                        }


                        R_m = (TextView) findViewById(R.id.rest_minute);
                        R_m.setText(String.valueOf(Total_R/6));
                        A_m = (TextView) findViewById(R.id.act_minute);
                        A_m.setText(String.valueOf(Total_A/6));
                        // 휴식 시간 그래프
                        BarDataSet datasetRest = new BarDataSet(entriesRest, "시간별 휴식 시간");
                        datasetRest.setColors(ColorTemplate.COLORFUL_COLORS);

                        // 차트에 데이터 입력
                        BarData dataRest = new BarData(datasetRest);
                        barChartRest.setData(dataRest);

                        // x축 설정
                        XAxis xAxisRest = barChartRest.getXAxis();
                        xAxisRest.setValueFormatter(new IndexAxisValueFormatter(labels));
                        xAxisRest.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxisRest.setAxisMinimum(0);
                        xAxisRest.setLabelCount(12);

                        // y축 설정
                        // 왼쪽 y축 제거
                        YAxis yLAxisRest = barChartRest.getAxisLeft();
                        yLAxisRest.setDrawLabels(false);
                        yLAxisRest.setDrawAxisLine(false);
                        yLAxisRest.setDrawGridLines(false);

                        barChartRest.setDoubleTapToZoomEnabled(false);
                        barChartRest.setDrawGridBackground(false);

                        Description descriptionRest = new Description();
                        descriptionRest.setText("분");
                        barChartRest.setDescription(descriptionRest);

                        barChartRest.animateY(1000); // 애니메이션 설정
                        barChartRest.invalidate();

                        // 운동 시간 그래프
                        BarDataSet datasetAct = new BarDataSet(entriesAct, "시간별 운동 시간");
                        datasetAct.setColors(ColorTemplate.COLORFUL_COLORS);

                        // 차트에 데이터 입력
                        BarData dataAct = new BarData(datasetAct);
                        barChartAct.setData(dataAct);

                        // x축 설정
                        XAxis xAxisAct = barChartAct.getXAxis();
                        xAxisAct.setValueFormatter(new IndexAxisValueFormatter(labels));
                        xAxisAct.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxisAct.setAxisMinimum(0);
                        xAxisAct.setLabelCount(12);

                        // y축 설정
                        // 왼쪽 y축 제거
                        YAxis yLAxisAct = barChartAct.getAxisLeft();
                        yLAxisAct.setDrawLabels(false);
                        yLAxisAct.setDrawAxisLine(false);
                        yLAxisAct.setDrawGridLines(false);

                        barChartAct.setDoubleTapToZoomEnabled(false);
                        barChartAct.setDrawGridBackground(false);

                        Description descriptionAct = new Description();
                        descriptionAct.setText("분");
                        barChartAct.setDescription(descriptionAct);

                        barChartAct.animateY(1000); // 애니메이션 설정
                        barChartAct.invalidate();
                    } else {
                        Log.d("CHARTACTION", "Error getting documents: ", task.getException());
                    }
                });
        //------------------------------------------------------------------------------------------


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH", Locale.KOREA);
        Date currentTime = new Date ( );
        stringTime = simpleDateFormat.format(currentTime);

        db.collection("Users")
                .document(userUid)
                .collection("Pets")
                .document(petNameStr)
                .collection("Actions")
                .get().addOnCompleteListener(task -> {
                    count = task.getResult().size()-1;
                    Log.d(TAG,"count : " + count);
                });

    }

    protected String subtract1Hour(int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        cal.add(Calendar.HOUR, -(i));
        return format.format(cal.getTime());
    }

    protected int secondToMinute(String second) {
        return parseInt(second) / 60;
    }
}
