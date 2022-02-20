package com.example.pet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        // Home 화면에서 pet 이름 받아오기
        Intent intentHome = new Intent(this.getIntent());
        String petNameStr = intentHome.getStringExtra("petName");

        // TODO: pet name
        TextView tvPetName = (TextView) findViewById(R.id.tv_petName);
        tvPetName.setText(petNameStr);

        // pet 추가 버튼
        ImageButton btnPetInfo = (ImageButton) findViewById(R.id.btn_petInfo);
        Intent intentInfo_PetInfo = new Intent(this, Profile_info.class);
        btnPetInfo.setOnClickListener(view -> {
            // pet 정보 보기 화면으로 넘어가기
            intentInfo_PetInfo.putExtra("petName", petNameStr);
            startActivity(intentInfo_PetInfo);
        });


        // 감정
        LinearLayout layoutEmotion = (LinearLayout) findViewById(R.id.layout_emotion);
        TextView tvEmotion = (TextView) findViewById(R.id.tv_emotion);
        ImageView ivEmotion = (ImageView) findViewById(R.id.iv_emotion);

        // TODO: emotion
        String emotionStr = "화남 / 불쾌";
        tvEmotion.setText(emotionStr);

        ivEmotion.setImageResource(R.drawable.ic_launcher_foreground);

        Intent intentEmotion = new Intent(this, Emotion_chart.class);
        layoutEmotion.setOnClickListener(view -> {
            startActivity(intentEmotion);
        });

        // 활동
        LinearLayout layoutAction = (LinearLayout) findViewById(R.id.layout_action);


        Intent intentAction = new Intent(this, Sleep_Chart.class);
        layoutAction.setOnClickListener(view -> {
            startActivity(intentAction);
        });

        // 통증
        LinearLayout layoutPain = (LinearLayout) findViewById(R.id.layout_pain);
        TextView tvPain = (TextView) findViewById(R.id.tv_pain);

        // TODO: pain
        String painStr = "2";
        // tvPain.setText(painStr);

        Intent intentPain = new Intent(this, Info_Pain.class);
        layoutPain.setOnClickListener(view -> {
            startActivity(intentPain);
        });

        // 이상행동
        LinearLayout layoutAbnormalBehavior = (LinearLayout) findViewById(R.id.layout_abnormal_behavior);
        TextView tvAbnormalBehavior = (TextView) findViewById(R.id.tv_abnormal_behavior);

        // TODO: pain
        String abnormalBehaviorStr = "6";
        // tvAbnormalBehavior.setText(abnormalBehaviorStr);

        Intent intentAbnormalBehavior = new Intent(this, Info_AbnormalBehavior.class);
        layoutAbnormalBehavior.setOnClickListener(view -> {
            startActivity(intentAbnormalBehavior);
        });
    }
}
