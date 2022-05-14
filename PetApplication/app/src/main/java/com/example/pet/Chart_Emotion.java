package com.example.pet;

import static android.content.ContentValues.TAG;

import static com.example.pet.Info.format_yyMMdd_HH;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class Chart_Emotion extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String happy;
    String relax;
    String unrest;
    String angry;
    String fear;
    String agg;

    TextView tvhappy;
    TextView tvrelax;
    TextView tvunrest;
    TextView tvangry;
    TextView tvfear;
    TextView tvagg;

    private FirebaseAuth firebaseAuth;
    Map<String, Object> Emotion = new HashMap<>();

    Date currentTime;
    SimpleDateFormat format;
    LineChart lineChartEmotion;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_emotion);

        currentTime = Calendar.getInstance().getTime();
        format = new SimpleDateFormat(format_yyMMdd_HH, Locale.getDefault());
        String currentTimeStr = format.format(currentTime);

        // 뒤로가기 버튼
        ImageButton btnEmotionBack = findViewById(R.id.btn_emotion_back);
        btnEmotionBack.setOnClickListener(view -> {
            finish();
        });

        Intent intentInfo = new Intent(this.getIntent());
        String petNameStr = intentInfo.getStringExtra("petName");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userUid = user.getUid();

        //DB Emotion Read
        DocumentReference docRef = db.collection("Users").document(userUid)
                .collection("Pets").document(petNameStr)
                .collection("Emotions").document(currentTimeStr);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Emotion = document.getData();
                        Log.d("NOOO", "No such document" + Emotion);

                        happy = String.valueOf(secondToMinute(String.valueOf(Emotion.get("행복"))));
                        relax = String.valueOf(secondToMinute(String.valueOf(Emotion.get("평안"))));
                        unrest =String.valueOf(secondToMinute(String.valueOf(Emotion.get("불안"))));
                        angry = String.valueOf(secondToMinute(String.valueOf(Emotion.get("화남"))));
                        fear = String.valueOf(secondToMinute(String.valueOf(Emotion.get("공포"))));
                        agg = String.valueOf(secondToMinute(String.valueOf(Emotion.get("공격성"))));

                        tvhappy = findViewById(R.id.emo_1);
                        tvrelax = findViewById(R.id.emo_2);
                        tvunrest = findViewById(R.id.emo_3);
                        tvangry = findViewById(R.id.emo_4);
                        tvfear = findViewById(R.id.emo_5);
                        tvagg = findViewById(R.id.emo_6 );

                        tvhappy.setText(happy);
                        tvrelax.setText(relax);
                        tvunrest.setText(unrest);
                        tvangry.setText(angry);
                        tvfear.setText(fear);
                        tvagg.setText(agg);

                    }
                    else{
                        Log.d(TAG, "No such document");
                    }
                }
                else{
                    Log.d(TAG, "get failed with", task.getException());
                }
            }
        });

        // 그래프
        //------------------------------------------------------------------------------------------
        lineChartEmotion = findViewById(R.id.emotion_chart);

        db.collection("Users").document(userUid)
                .collection("Pets").document(petNameStr)
                .collection("Emotions")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // x축 라벨
                        ArrayList<String> labels = new ArrayList<>();
                        labels.add("5h");
                        labels.add("4h");
                        labels.add("3h");
                        labels.add("2h");
                        labels.add("1h");
                        labels.add("0");

                        // 그래프에 값 추가
                        ArrayList<Entry> entriesHappy = new ArrayList<>();
                        ArrayList<Entry> entriesRelax = new ArrayList<>();
                        ArrayList<Entry> entriesAnxiety = new ArrayList<>();
                        ArrayList<Entry> entriesAngry = new ArrayList<>();
                        ArrayList<Entry> entriesFear = new ArrayList<>();
                        ArrayList<Entry> entriesAgg = new ArrayList<>();

                        for (int i = 0; i < 6; i++) {
                            entriesHappy.add(new BarEntry(i, 0));
                            entriesRelax.add(new BarEntry(i, 0));
                            entriesAnxiety.add(new BarEntry(i, 0));
                            entriesAngry.add(new BarEntry(i, 0));
                            entriesFear.add(new BarEntry(i, 0));
                            entriesAgg.add(new BarEntry(i, 0));
                        }

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String documentNameStr = document.getId();
                            if (documentNameStr.equals(subtract1Hour(5))) {
                                entriesHappy.set(0, new BarEntry(0, secondToMinute(String.valueOf(document.get("행복")))));
                                entriesRelax.set(0, new BarEntry(0, secondToMinute(String.valueOf(document.get("평안")))));
                                entriesAnxiety.set(0, new BarEntry(0, secondToMinute(String.valueOf(document.get("불안")))));
                                entriesAngry.set(0, new BarEntry(0, secondToMinute(String.valueOf(document.get("화남")))));
                                entriesFear.set(0, new BarEntry(0, secondToMinute(String.valueOf(document.get("공포")))));
                                entriesAgg.set(0, new BarEntry(0, secondToMinute(String.valueOf(document.get("공격성")))));
                            } else if (documentNameStr.equals(subtract1Hour(4))) {
                                entriesHappy.set(1, new BarEntry(1, secondToMinute(String.valueOf(document.get("행복")))));
                                entriesRelax.set(1, new BarEntry(1, secondToMinute(String.valueOf(document.get("평안")))));
                                entriesAnxiety.set(1, new BarEntry(1, secondToMinute(String.valueOf(document.get("불안")))));
                                entriesAngry.set(1, new BarEntry(1, secondToMinute(String.valueOf(document.get("화남")))));
                                entriesFear.set(1, new BarEntry(1, secondToMinute(String.valueOf(document.get("공포")))));
                                entriesAgg.set(1, new BarEntry(1, secondToMinute(String.valueOf(document.get("공격성")))));
                            } else if (documentNameStr.equals(subtract1Hour(3))) {
                                entriesHappy.set(2, new BarEntry(2, secondToMinute(String.valueOf(document.get("행복")))));
                                entriesRelax.set(2, new BarEntry(2, secondToMinute(String.valueOf(document.get("평안")))));
                                entriesAnxiety.set(2, new BarEntry(2, secondToMinute(String.valueOf(document.get("불안")))));
                                entriesAngry.set(2, new BarEntry(2, secondToMinute(String.valueOf(document.get("화남")))));
                                entriesFear.set(2, new BarEntry(2, secondToMinute(String.valueOf(document.get("공포")))));
                                entriesAgg.set(2, new BarEntry(2, secondToMinute(String.valueOf(document.get("공격성")))));
                            } else if (documentNameStr.equals(subtract1Hour(2))) {
                                entriesHappy.set(3, new BarEntry(3, secondToMinute(String.valueOf(document.get("행복")))));
                                entriesRelax.set(3, new BarEntry(3, secondToMinute(String.valueOf(document.get("평안")))));
                                entriesAnxiety.set(3, new BarEntry(3, secondToMinute(String.valueOf(document.get("불안")))));
                                entriesAngry.set(3, new BarEntry(3, secondToMinute(String.valueOf(document.get("화남")))));
                                entriesFear.set(3, new BarEntry(3, secondToMinute(String.valueOf(document.get("공포")))));
                                entriesAgg.set(3, new BarEntry(3, secondToMinute(String.valueOf(document.get("공격성")))));
                            } else if (documentNameStr.equals(subtract1Hour(1))) {
                                entriesHappy.set(4, new BarEntry(4, secondToMinute(String.valueOf(document.get("행복")))));
                                entriesRelax.set(4, new BarEntry(4, secondToMinute(String.valueOf(document.get("평안")))));
                                entriesAnxiety.set(4, new BarEntry(4, secondToMinute(String.valueOf(document.get("불안")))));
                                entriesAngry.set(4, new BarEntry(4, secondToMinute(String.valueOf(document.get("화남")))));
                                entriesFear.set(4, new BarEntry(4, secondToMinute(String.valueOf(document.get("공포")))));
                                entriesAgg.set(4, new BarEntry(4, secondToMinute(String.valueOf(document.get("공격성")))));
                            } else if (documentNameStr.equals(currentTimeStr)) {
                                entriesHappy.set(5, new BarEntry(5, secondToMinute(String.valueOf(document.get("행복")))));
                                entriesRelax.set(5, new BarEntry(5, secondToMinute(String.valueOf(document.get("평안")))));
                                entriesAnxiety.set(5, new BarEntry(5, secondToMinute(String.valueOf(document.get("불안")))));
                                entriesAngry.set(5, new BarEntry(5, secondToMinute(String.valueOf(document.get("화남")))));
                                entriesFear.set(5, new BarEntry(5, secondToMinute(String.valueOf(document.get("공포")))));
                                entriesAgg.set(5, new BarEntry(5, secondToMinute(String.valueOf(document.get("공격성")))));
                            }
                        }

                        LineDataSet dataSetHappy = new LineDataSet(entriesHappy, "행복");
                        dataSetHappy.setColors(Color.BLACK);
                        LineDataSet dataSetRelax = new LineDataSet(entriesRelax, "평안");
                        dataSetRelax.setColors(Color.RED);
                        LineDataSet dataSetAnxiety = new LineDataSet(entriesAnxiety, "불안");
                        dataSetAnxiety.setColors(Color.BLUE);
                        LineDataSet dataSetAngry = new LineDataSet(entriesAngry, "화남");
                        dataSetAngry.setColors(Color.GREEN);
                        LineDataSet dataSetFear = new LineDataSet(entriesFear, "공포");
                        dataSetFear.setColors(Color.GRAY);
                        LineDataSet dataSetAgg = new LineDataSet(entriesAgg, "공격성");
                        dataSetAgg.setColors(Color.YELLOW);

                        // 차트에 데이터 입력
                        LineData data = new LineData();
                        data.addDataSet(dataSetHappy);
                        data.addDataSet(dataSetRelax);
                        data.addDataSet(dataSetAnxiety);
                        data.addDataSet(dataSetAngry);
                        data.addDataSet(dataSetFear);
                        data.addDataSet(dataSetAgg);
                        lineChartEmotion.setData(data);

                        // x축 설정
                        XAxis xAxis = lineChartEmotion.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setAxisMinimum(0);
                        xAxis.setLabelCount(10);

                        // y축 설정
                        // 왼쪽 y축 제거
                        YAxis yLAxis = lineChartEmotion.getAxisLeft();
                        yLAxis.setDrawLabels(false);
                        yLAxis.setDrawAxisLine(false);
                        yLAxis.setDrawGridLines(false);

                        lineChartEmotion.setDoubleTapToZoomEnabled(false);
                        lineChartEmotion.setDrawGridBackground(false);

                        Description description = new Description();
                        description.setText("분");
                        lineChartEmotion.setDescription(description);

                        lineChartEmotion.animateY(1000); // 애니메이션 설정
                        lineChartEmotion.invalidate();
                    } else {
                        Log.d("CHARTEMOTION", "Error getting documents: ", task.getException());
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

    protected int secondToMinute(String second) {
        return parseInt(second) / 60;
    }
}
