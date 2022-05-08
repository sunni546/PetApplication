package com.example.pet;


import static android.content.ContentValues.TAG;

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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Chart_Emotion<Int> extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String happy;
    String comfort;
    String anxiety;
    String angry;
    String fear;
    String agg;

    TextView tvhappy;
    TextView tvcomfort;
    TextView tvanxiety;
    TextView tvangry;
    TextView tvfear;
    TextView tvagg;

    public static String format_yyyyMMdd_HHmm = "yyMMdd_hh";

    private FirebaseAuth firebaseAuth;
    Map<String, Object> Emotion = new HashMap<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_emotion);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat(format_yyyyMMdd_HHmm, Locale.getDefault());
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
                        Log.d("NOOO", "No such document"+Emotion);

                        happy = String.valueOf(Emotion.get("행복"));
                        comfort = String.valueOf(Emotion.get("평안"));
                        anxiety =String.valueOf(Emotion.get("불안"));
                        angry = String.valueOf(Emotion.get("화남"));
                        fear = String.valueOf(Emotion.get("공포"));
                        agg = String.valueOf(Emotion.get("공격성"));

                        tvhappy = findViewById(R.id.emo_1);
                        tvcomfort = findViewById(R.id.emo_2);
                        tvanxiety = findViewById(R.id.emo_3);
                        tvangry = findViewById(R.id.emo_4);
                        tvfear = findViewById(R.id.emo_5);
                        tvagg = findViewById(R.id.emo_6 );

                        tvhappy.setText(happy);
                        tvcomfort.setText(comfort);
                        tvanxiety.setText(anxiety);
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


        //Draw Chart
        LineChart lineChart = (LineChart) findViewById(R.id.emotion_chart);

        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<Entry> entries2 = new ArrayList<>();
        ArrayList<Entry> entries3 = new ArrayList<>();
        ArrayList<Entry> entries4 = new ArrayList<>();
        ArrayList<Entry> entries5 = new ArrayList<>();
        ArrayList<Entry> entries6 = new ArrayList<>();

        entries.add(new Entry(0, 3));
        entries.add(new Entry(1, 1));
        entries.add(new Entry(2, 2));
        entries.add(new Entry(3, 3));
        entries.add(new Entry(4, 4));
        entries.add(new Entry(5, 5));

        entries2.add(new Entry(0, 0));
        entries2.add(new Entry(1, 2));
        entries2.add(new Entry(2, 3));
        entries2.add(new Entry(3, 4));
        entries2.add(new Entry(4, 5));
        entries2.add(new Entry(5, 6));

        entries3.add(new Entry(0, 0));
        entries3.add(new Entry(1, 2));
        entries3.add(new Entry(2, 2));
        entries3.add(new Entry(3, 9));
        entries3.add(new Entry(4, 1));
        entries3.add(new Entry(5, 1));

        entries4.add(new Entry(0, 0));
        entries4.add(new Entry(1, 2));
        entries4.add(new Entry(2, 3));
        entries4.add(new Entry(3, 1));
        entries4.add(new Entry(4, 0));
        entries4.add(new Entry(5, 3));

        entries5.add(new Entry(0, 0));
        entries5.add(new Entry(1, 1));
        entries5.add(new Entry(2, 3));
        entries5.add(new Entry(3, 2));
        entries5.add(new Entry(4, 4));
        entries5.add(new Entry(5, 3));

        entries6.add(new Entry(0, 0));
        entries6.add(new Entry(1, 5));
        entries6.add(new Entry(2, 4));
        entries6.add(new Entry(3, 3));
        entries6.add(new Entry(4, 2));
        entries6.add(new Entry(5, 1));

        LineDataSet dataSet = new LineDataSet(entries, "행복/즐거움");
        dataSet.setColors(Color.BLACK);

        LineDataSet dataSet2 = new LineDataSet(entries2, "평안/안정");
        dataSet2.setColors(Color.RED);

        LineDataSet dataSet3 = new LineDataSet(entries3, "불안/슬픔");
        dataSet3.setColors(Color.BLUE);

        LineDataSet dataSet4 = new LineDataSet(entries4, "화남/불쾌");
        dataSet4.setColors(Color.GREEN);

        LineDataSet dataSet5 = new LineDataSet(entries5, "공포");
        dataSet5.setColors(Color.GRAY);

        LineDataSet dataSet6 = new LineDataSet(entries6, "공격성");
        dataSet6.setColors(Color.YELLOW);

        LineData data = new LineData();
        data.addDataSet(dataSet);
        data.addDataSet(dataSet2);
        data.addDataSet(dataSet3);
        data.addDataSet(dataSet4);
        data.addDataSet(dataSet5);
        data.addDataSet(dataSet6);

        lineChart.setData(data);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(10, 24, 0);
        xAxis.setAxisMinimum(0);
        xAxis.setLabelCount(3);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);

        Description description = new Description();
        description.setText("Time");

        lineChart.setDescription(description);
        lineChart.animateY(2000);
        lineChart.invalidate();


    }
}
