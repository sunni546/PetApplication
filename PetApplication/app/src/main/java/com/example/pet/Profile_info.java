package com.example.pet;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profile_info extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_info);

        // Info 화면에서 pet 이름 받아오기
        Intent intentInfo = new Intent(this.getIntent());
        String petNameStr = intentInfo.getStringExtra("petName");

        // TODO: pet name
        TextView tvPetNamePI = (TextView) findViewById(R.id.tv_pet_name_profile_info);
        tvPetNamePI.setText(petNameStr);

        //취소 버튼
        Button cancel_btn = (Button) findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(view -> {
            finish();
        });

        // 저장 버튼
        Button save_btn = (Button) findViewById(R.id.save_btn);
        save_btn.setOnClickListener(view -> {
            finish();
        });

    }
}
