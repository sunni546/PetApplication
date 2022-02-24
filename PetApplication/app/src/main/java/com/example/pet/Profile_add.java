package com.example.pet;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Profile_add extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = null;
    private FirebaseFirestore firebaseFirestore = null;

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


                // TODO : 통증 이상횟수 추가할 곳이 없어서 일단 여기에서
                Map<String, Object> Pain = new HashMap<>();
                Pain.put("numPain", 1);
                Pain.put("timePain", new Timestamp(new Date()));
                Map<String, Object> AB = new HashMap<>();
                AB.put("numAB", 2);
                AB.put("timeAB", new Timestamp(new Date()));

                DocumentReference newPet = firebaseFirestore.collection("Users").document(userUid)
                        .collection("Pets").document();
                newPet.collection("Pains").document().set(Pain)
                        .addOnFailureListener(e -> Log.w("DB_Pet_Pain", "error", e));
                newPet.collection("AbnormalBehaviors").document().set(AB)
                        .addOnFailureListener(e -> Log.w("DB_Pet_AB", "error", e));
                // TODO : 여기까지


                /*
                // pet 정보 DB 에 추가
                DB_Pet db_pet = new DB_Pet();
                firebaseFirestore.collection("Users").document(userUid)
                        .collection("Pets").document().set(db_pet)
                        .addOnFailureListener(e -> Log.w("DB_Pet", "error", e));
                */

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
