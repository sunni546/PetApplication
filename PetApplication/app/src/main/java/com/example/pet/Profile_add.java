package com.example.pet;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Profile_add extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = null;
    private FirebaseFirestore firebaseFirestore = null;
    String stringtime;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_add);

        //firebase 인스턴스 초기화
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //취소 버튼
        Button cancel_btn = (Button) findViewById(R.id.cancel_btn_add);
        cancel_btn.setOnClickListener(view -> {
            finish();
        });

        // 저장 버튼
        Button save_btn = (Button) findViewById(R.id.save_btn_add);
        save_btn.setOnClickListener(view -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                // DB 에 User 저장
                String userUid = user.getUid();

                //이름
                EditText name_et = findViewById(R.id.name);
                String name = name_et.getText().toString();

                //성별
                RadioGroup gender_rg = findViewById(R.id.gender);
                int num = gender_rg.getCheckedRadioButtonId();
                RadioButton gender_rb = findViewById(num);
                String gender = gender_rb.getText().toString();

                //나이
                EditText age_et = findViewById(R.id.age);
                String age = age_et.getText().toString();

                //Cat/Dog
                RadioGroup cd_rg = findViewById(R.id.c_d);
                int num1 = cd_rg.getCheckedRadioButtonId();
                RadioButton cd_rb = findViewById(num1);
                String c_d = cd_rb.getText().toString();

                //종
                EditText breed_et = findViewById(R.id.breed);
                String breed = breed_et.getText().toString();

                //중성화
                RadioGroup neuter_rg = findViewById(R.id.neuter);
                int num2 = neuter_rg.getCheckedRadioButtonId();
                RadioButton neuter_rb = findViewById(num2);
                String neuter = neuter_rb.getText().toString();

                //질병
                RadioGroup disease_rg = findViewById(R.id.disease);
                int num3 = disease_rg.getCheckedRadioButtonId();
                RadioButton disease_rb = findViewById(num3);
                String disease = disease_rb.getText().toString();

                DocumentReference newPet = firebaseFirestore.collection("Users").document(userUid)
                        .collection("Pets").document(name);

                // TODO : 동물 정보 추가
                Map<String, String> Info = new HashMap<>();
                Info.put("name", name);
                Info.put("gender", gender);
                Info.put("age", age);
                Info.put("cat_dog", c_d);
                Info.put("breed", breed);
                Info.put("neuter", neuter);
                Info.put("disease", disease);
                newPet.set(Info)
                        .addOnFailureListener(e -> Log.w("DB_Pet_Info", "error", e));

                // TODO : 감정
                Map<String, Object> Emotion = new HashMap<>();
                Emotion.put("행복/즐거움", 1);
                Emotion.put("평안/안정", 2);
                Emotion.put("불안/슬픔", 3);
                Emotion.put("화남/불쾌", 4);
                Emotion.put("공포", 5);
                Emotion.put("공격성", 6);
                newPet.collection("Emotion").document("Emotion").set(Emotion)
                        .addOnFailureListener(e -> Log.w("DB_Pet_Emotion", "error", e));

                // TODO : 활동
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh", Locale.KOREA);
                Calendar calendar = Calendar.getInstance();
                stringtime=simpleDateFormat.format(calendar.getTime());
                Map<String, Object> Act = new HashMap<>();
                Act.put("수면 시간", 1);
                Act.put("식사 시간", 2);
                Act.put("운동 시간",3);
                newPet.collection("Act").document(stringtime).set(Act)
                        .addOnFailureListener(e -> Log.w("DB_Pet_Act", "error", e));

                // TODO : 통증
                Map<String, Object> Pain = new HashMap<>();
                Pain.put("numPain", 1);
                Pain.put("timePain", new Timestamp(new Date()));
                newPet.collection("Pains").document("Pains").set(Pain)
                        .addOnFailureListener(e -> Log.w("DB_Pet_Pain", "error", e));

                // TODO : 이상횟수
                Map<String, Object> AB = new HashMap<>();
                AB.put("numAB", 2);
                AB.put("timeAB", new Timestamp(new Date()));
                newPet.collection("AbnormalBehaviors").document("AbnormalBehaviors").set(AB)
                        .addOnFailureListener(e -> Log.w("DB_Pet_AB", "error", e));

                // 팝업 끄기
                // Home 화면으로
                finish();
            } else {
                // No user is signed in
                // 실패
                Log.w("DB_Pet", "error: user is null");
            }
        });

    }
}
