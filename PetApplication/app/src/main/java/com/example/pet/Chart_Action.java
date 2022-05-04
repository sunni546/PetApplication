package com.example.pet;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Chart_Action extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    int count;
    String stringtime;

    String sleep;
    int sleep_t;

    String act;
    int act_t;

    TextView sleep_h;
    TextView sleep_m;

    TextView act_h;
    TextView act_m;

    Map<String, Object> Action = new HashMap<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_action);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userUid = user.getUid();
        Intent intentInfo = new Intent(this.getIntent());
        String petNameStr = intentInfo.getStringExtra("petName");

        // 뒤로가기 버튼
        ImageButton btnActionBack = (ImageButton) findViewById(R.id.btn_action_back);
        btnActionBack.setOnClickListener(view -> {
            finish();
        });

        //수면시간 차트
        BarChart barChart = (BarChart) findViewById(R.id.sleep_chart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        BarDataSet dataSet = new BarDataSet(entries, "# of Calls");
        BarData data = new BarData(dataSet);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS); //
        barChart.setData(data);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh", Locale.KOREA);
        Date currentTime = new Date ( );
        stringtime=simpleDateFormat.format(currentTime);

        db.collection("Users")
                .document(userUid)
                .collection("Pets")
                .document(petNameStr)
                .collection("Actions")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                count = task.getResult().size()-1;
                Log.d(TAG,"count : "+count);
            }
        });

        DocumentReference docRef = db.collection("Users").document(userUid)
                .collection("Pets").document(petNameStr)
                .collection("Actions").document("TOTAL");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Action = document.getData();
                        sleep = String.valueOf(Action.get("수면 시간"));
                        act =String.valueOf(Action.get("활동 시간"));

                        sleep_t=(Integer.parseInt(sleep)/count);
                        act_t=(Integer.parseInt(act)/count);

                        sleep_h = findViewById(R.id.sleep_hour);
                        sleep_m = findViewById(R.id.sleep_minute);
                        sleep_h.setText(String.valueOf(sleep_t/60));
                        sleep_m.setText(String.valueOf(sleep_t%60));

                        act_h = findViewById(R.id.act_hour);
                        act_m = findViewById(R.id.act_minute);
                        act_h.setText(String.valueOf(act_t/60));
                        act_m.setText(String.valueOf(act_t%60));
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
    }
}
