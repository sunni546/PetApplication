package com.example.pet;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Info extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String happy;
    String comfort;
    String anxiety;
    String angry;
    String fear;
    String agg;
    String emotionStr;
    String max_value;

    String total_sleep;
    String total_eat;
    String total_act;

    TextView total_sleep_h;
    TextView total_sleep_m;

    TextView total_eat_h;
    TextView total_eat_m;

    TextView total_act_h;
    TextView total_act_m;


    private FirebaseAuth firebaseAuth;
    Map<String, Object> Emotion = new HashMap<>();
    Map<String, String> sampleMap = new HashMap<>();
    Map<String, Object> Action = new HashMap<>();
    ArrayList<Integer> num = new ArrayList<>();
    Object obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        // Home 화면에서 pet 이름 받아오기
        Intent intentHome = new Intent(this.getIntent());
        String petNameStr = intentHome.getStringExtra("petName");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userUid = user.getUid();

        // TODO: pet name
        TextView tvPetName = (TextView) findViewById(R.id.tv_petName);
        tvPetName.setText(petNameStr);

        // pet info 버튼
        ImageButton btnPetInfo = (ImageButton) findViewById(R.id.btn_petInfo);
        Intent intentProfile_info = new Intent(this, Profile_info.class);
        btnPetInfo.setOnClickListener(view -> {
            // pet 정보 보기 화면으로 넘어가기
            intentProfile_info.putExtra("petName", petNameStr);
            startActivity(intentProfile_info);
        });

        // 감정
        LinearLayout layoutEmotion = (LinearLayout) findViewById(R.id.layout_emotion);
        TextView tvEmotion = (TextView) findViewById(R.id.tv_emotion);
        ImageView ivEmotion = (ImageView) findViewById(R.id.iv_emotion);

        // TODO: emotion
        //---------------------------Emotion 중 가장 높은 값을 현재 감정 상태로------------------------
        DocumentReference docRef = db.collection("Users").document(userUid)
                .collection("Pets").document(petNameStr)
                .collection("Emotions").document("Emotions");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Emotion = document.getData();

                        happy = String.valueOf(Emotion.get("행복/즐거움"));
                        comfort = String.valueOf(Emotion.get("평안/안정"));
                        anxiety =String.valueOf(Emotion.get("불안/슬픔"));
                        angry = String.valueOf(Emotion.get("화남/불쾌"));
                        fear = String.valueOf(Emotion.get("공포"));
                        agg = String.valueOf(Emotion.get("공격성"));

                        num.add(Integer.parseInt(happy));
                        num.add(Integer.parseInt(comfort));
                        num.add(Integer.parseInt(anxiety));
                        num.add(Integer.parseInt(angry));
                        num.add(Integer.parseInt(fear));
                        num.add(Integer.parseInt(agg));

                        max_value = String.valueOf(Collections.max(num));

                        sampleMap.put("행복/즐거움",happy);
                        sampleMap.put("평안/안정",comfort);
                        sampleMap.put("불안/슬픔",anxiety );
                        sampleMap.put("화남/불쾌",angry );
                        sampleMap.put("공포",fear);
                        sampleMap.put("공격성",agg);

                        emotionStr = getSingleKeyFromValue(sampleMap, max_value);
                        tvEmotion.setText(emotionStr);
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

        ivEmotion.setImageResource(R.drawable.ic_launcher_foreground);

        Intent intentEmotion = new Intent(this, Chart_Emotion.class);
        layoutEmotion.setOnClickListener(view -> {
            // pet 정보 화면으로 넘어가기
            intentEmotion.putExtra("petName", petNameStr);
            startActivity(intentEmotion);
        });
        //------------------------------------------------------------------------------------------

        // 활동 ------------------------------------------------------------------------------------
        LinearLayout layoutAction = (LinearLayout) findViewById(R.id.layout_action);

        DocumentReference docRe2 = db.collection("Users").document(userUid)
                .collection("Pets").document(petNameStr)
                .collection("Actions").document("TOTAL");
        docRe2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Action = document.getData();
                        total_sleep = String.valueOf(Action.get("수면 시간"));
                        total_eat = String.valueOf(Action.get("식사 시간"));
                        total_act = String.valueOf(Action.get("활동 시간"));

                        total_sleep_h = findViewById(R.id.tv_h_sleep);
                        total_sleep_m = findViewById(R.id.tv_m_sleep);
                        total_sleep_h.setText(String.valueOf(Integer.parseInt(total_sleep)/60));
                        total_sleep_m.setText(String.valueOf(Integer.parseInt(total_sleep)%60));

                        total_eat_h = findViewById(R.id.tv_h_eat);
                        total_eat_m = findViewById(R.id.tv_m_eat);
                        total_eat_h.setText(String.valueOf(Integer.parseInt(total_eat)/60));
                        total_eat_m.setText(String.valueOf(Integer.parseInt(total_eat)%60));

                        total_act_h = findViewById(R.id.tv_h_movement);
                        total_act_m = findViewById(R.id.tv_m_movement);
                        total_act_h.setText(String.valueOf(Integer.parseInt(total_act)/60));
                        total_act_m.setText(String.valueOf(Integer.parseInt(total_act)%60));
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



        Intent intentAction = new Intent(this, Chart_Action.class);
        layoutAction.setOnClickListener(view -> {
            intentAction.putExtra("petName", petNameStr);
            startActivity(intentAction);
        });
        //------------------------------------------------------------------------------------------


        // 이상행동
        LinearLayout layoutAbnormalBehavior = (LinearLayout) findViewById(R.id.layout_abnormal_behavior);
        TextView tvAbnormalBehavior = (TextView) findViewById(R.id.tv_abnormal_behavior);

        // TODO: 이상행동
        String abnormalBehaviorStr = "6";
        // tvAbnormalBehavior.setText(abnormalBehaviorStr);

        Intent intentAbnormalBehavior = new Intent(this, Chart_AbnormalBehavior.class);
        layoutAbnormalBehavior.setOnClickListener(view -> {
            startActivity(intentAbnormalBehavior);
        });

        DocumentReference docRef2 = db.collection("Users").document(userUid)
                .collection("Pets").document(petNameStr);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("PETCCTVURL", "DocumentSnapshot data: " + document.getData());
                        String cctvUrlStr = document.get("cctvUrl").toString();

                        // cctv 버튼
                        FloatingActionButton fab = findViewById(R.id.fab);
                        fab.setOnClickListener((view -> {
                            // cctv 화면으로 넘어가기
                            Intent intentCctv = new Intent(getApplicationContext(), Cctv.class);
                            intentCctv.putExtra("petName", petNameStr);
                            intentCctv.putExtra("cctvUrl", cctvUrlStr);
                            startActivity(intentCctv);
                        }));
                    } else {
                        Log.d("PETCCTVURL", "No such document");
                    }
                } else {
                    Log.d("PETCCTVURL", "get failed with ", task.getException());
                }
            }
        });
    }

    //Value값을 이용해서 Key값을 찾는 함수
    public static <K, V> K getSingleKeyFromValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

}



