package com.example.pet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Log_In extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        // 로그인
        Button btnLogIn = (Button) findViewById(R.id.btn_log_in);
        Intent intentHome = new Intent(this, Home.class);
        btnLogIn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // pet 추가 화면으로 넘어가기
                startActivity(intentHome);
            }
        });
    }
}