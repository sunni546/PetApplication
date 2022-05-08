package com.example.pet;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Chart_AbnormalBehavior extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String format_yyyyMMdd_HHmm = "yyMMdd_hh";
    public static String format_yyyyMMdd_HHmm2 = "yyMMdd_";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_abnormal_behavior);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat(format_yyyyMMdd_HHmm, Locale.getDefault());
        SimpleDateFormat format2 = new SimpleDateFormat(format_yyyyMMdd_HHmm2, Locale.getDefault());
        String currentTimeStr = format.format(currentTime);
        String currentTimeStr2 = format2.format(currentTime);

        // Home 화면에서 pet 이름 받아오기
        Intent intentHome = new Intent(this.getIntent());
        petNameStr = intentHome.getStringExtra("petName");
        Log.d("NAME", "Pet name : " + petNameStr);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userUid = user.getUid();
        Total_a=0;

        // 뒤로가기 버튼
        ImageButton btnAbBack = findViewById(R.id.btn_ab_back);
        btnAbBack.setOnClickListener(view -> {
            // pet 정보 화면으로 넘어가기
            finish();
        });

        // 이상 행동 통계
        tv1HourAb = (TextView) findViewById(R.id.tv_1hour_ab);
        tv1dDayAb = (TextView) findViewById(R.id.tv_1day_ab);

        DocumentReference docRef = db.collection("Users").document(userUid)
                .collection("Pets").document(petNameStr)
                .collection("AbnormalBehaviors").document(currentTimeStr);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        Abnormal2=document.getData();
                        ACount_st2 = String.valueOf(Abnormal2.get("count"));
                        Time2 = String.valueOf(Abnormal2.get("time"));
                        tv1HourAb.setText(ACount_st2);
                    }
                }
            }
        });

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