package com.example.pet;


import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Profile_add extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_add);

        //취소 버튼
        Button cancel_btn = (Button) findViewById(R.id.cancel_btn_add);
        cancel_btn.setOnClickListener(view -> {
            finish();
        });

        // 저장 버튼
        Button save_btn = (Button) findViewById(R.id.save_btn_add);
        save_btn.setOnClickListener(view -> {
            finish();
        });

    }
}
