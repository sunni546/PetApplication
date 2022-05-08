package com.example.pet;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pet.Network.BitmapThread;
import com.example.pet.Network.Networking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class Profile_add extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = null;
    private FirebaseFirestore firebaseFirestore = null;

    private String name;
    private String cctvUrl;

    private FFmpegMediaMetadataRetriever mediaMetadataRetriever;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_add);

        // firebase 인스턴스 초기화
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // 뒤로가기 버튼
        ImageButton cancel_btn = (ImageButton) findViewById(R.id.btn_petAdd_back);
        cancel_btn.setOnClickListener(view -> finish());

        // Cat/Dog 프사 바꾸기
        ImageView circleImageView = findViewById(R.id.circle_image_view);
        RadioGroup cd_rg = findViewById(R.id.c_d);
        // 라디오 그룹 클릭 리스너
        cd_rg.setOnCheckedChangeListener ((group, checkedId) -> {
            if (checkedId == R.id.cd_rb_cat) {
                circleImageView.setImageResource(R.drawable.img_9);
            } else if (checkedId == R.id.cd_rb_dog) {
                circleImageView.setImageResource(R.drawable.img_3);
            }
        });

        // 저장 버튼
        ImageButton save_btn = (ImageButton) findViewById(R.id.save_btn_add);
        save_btn.setOnClickListener(view -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                // DB 에 User 저장
                String userUid = user.getUid();

                // 이름
                EditText name_et = findViewById(R.id.name);
                name = name_et.getText().toString();

                // 성별
                RadioGroup gender_rg = findViewById(R.id.gender);
                int num = gender_rg.getCheckedRadioButtonId();
                RadioButton gender_rb = findViewById(num);
                String gender = gender_rb.getText().toString();

                // 나이
                EditText age_et = findViewById(R.id.age);
                String age = age_et.getText().toString();

                // Cat/Dog
                int num1 = cd_rg.getCheckedRadioButtonId();
                RadioButton cd_rb = findViewById(num1);
                String c_d = cd_rb.getText().toString();

                // 종
                EditText breed_et = findViewById(R.id.breed);
                String breed = breed_et.getText().toString();

                // 중성화
                RadioGroup neuter_rg = findViewById(R.id.neuter);
                int num2 = neuter_rg.getCheckedRadioButtonId();
                RadioButton neuter_rb = findViewById(num2);
                String neuter = neuter_rb.getText().toString();

                // 질병
                RadioGroup disease_rg = findViewById(R.id.disease);
                int num3 = disease_rg.getCheckedRadioButtonId();
                RadioButton disease_rb = findViewById(num3);
                String disease = disease_rb.getText().toString();

                // cctv_url
                EditText cctvUrl_et = findViewById(R.id.cctv_url);
                cctvUrl = cctvUrl_et.getText().toString();

                DocumentReference newPet = firebaseFirestore.collection("Users").document(userUid)
                        .collection("Pets").document(name);

                // 동물 정보 추가
                Map<String, String> Info = new HashMap<>();
                Info.put("name", name);
                Info.put("gender", gender);
                Info.put("age", age);
                Info.put("cat_dog", c_d);
                Info.put("breed", breed);
                Info.put("neuter", neuter);
                Info.put("disease", disease);
                Info.put("cctvUrl", cctvUrl);
                newPet.set(Info)
                        .addOnFailureListener(e -> Log.w("DB_Pet_Info", "error", e));

                // 사용자의 pet 수 수정
                DocumentReference userOfPet = firebaseFirestore.collection("Users").document(userUid);
                userOfPet.update("numPets", FieldValue.increment(1));

                // 팝업 끄기
                // Home 화면으로
                ((Home)Home.hContext).makeListviewPet();
                finish();
            } else {
                // No user is signed in
                // 실패
                Log.w("DB_Pet", "error: user is null");
            }

            // BitmapThread
            mediaMetadataRetriever = new FFmpegMediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(cctvUrl);
            mediaMetadataRetriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);
            mediaMetadataRetriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST);

            BitmapThread bitmapThread = new BitmapThread(mediaMetadataRetriever);
            bitmapThread.start();

            Networking networking = new Networking(name);
            networking.start();

            // mediaMetadataRetriever.release();

        });
    }
}
