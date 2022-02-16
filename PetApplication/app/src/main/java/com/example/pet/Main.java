package com.example.pet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 로그인
        Button btnLogInM = (Button) findViewById(R.id.btn_log_in_main);
        Intent intentLogIn = new Intent(this, Log_In.class);
        btnLogInM.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // pet 추가 화면으로 넘어가기
                startActivity(intentLogIn);
            }
        });

        // 회원가입
        Button btnJoinM = (Button) findViewById(R.id.btn_join_main);
        Intent intentJoin = new Intent(this, Join.class);
        btnJoinM.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // pet 추가 화면으로 넘어가기
                startActivity(intentJoin);
            }
        });
    }
}